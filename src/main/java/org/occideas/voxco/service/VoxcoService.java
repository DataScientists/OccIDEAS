package org.occideas.voxco.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.Constant;
import org.occideas.entity.NodeVoxco;
import org.occideas.interview.service.InterviewService;
import org.occideas.module.service.ModuleService;
import org.occideas.node.service.INodeService;
import org.occideas.participant.service.ParticipantService;
import org.occideas.utilities.CsvUtil;
import org.occideas.utilities.ZipUtil;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.ParticipantVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.voxco.dao.INodeVoxcoDao;
import org.occideas.voxco.model.Block;
import org.occideas.voxco.model.Choice;
import org.occideas.voxco.model.ChoiceSettings;
import org.occideas.voxco.model.Extraction;
import org.occideas.voxco.model.Question;
import org.occideas.voxco.model.Survey;
import org.occideas.voxco.model.TranslatedTextContent;
import org.occideas.voxco.model.TranslatedTexts;
import org.occideas.voxco.model.User;
import org.occideas.voxco.model.Variable;
import org.occideas.voxco.request.SurveyImportRequest;
import org.occideas.voxco.response.ExtractionResult;
import org.occideas.voxco.response.SurveyExtractionsResult;
import org.occideas.voxco.response.SurveyImportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VoxcoService implements IVoxcoService {
    private static final Logger log = LogManager.getLogger(VoxcoService.class);

    private static final String LOGIC_BASIC = "logic:basic;";
    private static final String LOGIC_NOT_EQUAL = " != ";
    private static final String LOGIC_EQUAL = " = ";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${voxco.download.filepath.pre}")
    private String downloadPath;

    @Value("${voxco.extracton.result.fetch.retry:3}")
    private int resultRetryThreshold;

    @Autowired
    private INodeVoxcoDao voxcoDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private INodeService nodeService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private IVoxcoClient<Survey, Long> surveyClient;

    @Autowired
    private IVoxcoClient<User, Long> userClient;

    @Autowired
    private IVoxcoClient<Extraction, Long> resultClient;

    private int choiceId;

    private int resultFetchCounter;

    private String generateName(String name, String key) {
        return name + "_" + key;
    }

    private String getNodeKey(String nodeName) {
        return nodeName.substring(0, 4);
    }

    @Override
    @Transactional
    public void importSurvey(Long id) {
        importSurvey(id, false);
    }

    @Override
    @Transactional
    public void importSurvey(Long id, boolean filter) {
        List<ModuleVO> modules = moduleService.findById(id);
        if (!modules.isEmpty()) {
            ModuleVO module = modules.get(0);
            List<Survey> surveys = userClient.getUserSurveys().getBody();
            Long surveyId;
            String name = generateName(module.getName(), String.valueOf(module.getIdNode()));
            Survey matched = getSurveyByName(surveys, name);
            if (matched != null) {
                surveyId = matched.getId();
            } else {
                surveyId = getMaxSurveyId(surveys) + 1;
            }

            Survey survey = createOrUpdateSurvey(surveyId, name, module.getDescription());
            voxcoDao.save(surveyId, module.getIdNode(), survey.getName());
            buildAndImportSurvey(module, survey, filter);
        }
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void importAllToVoxco() {
        log.debug("importing all active job modules to voxco");
        List<ModuleVO> modules = moduleService.listAll();
        for (ModuleVO module : modules) {
            importSurvey(module.getIdNode());
        }
    }

    @Override
    @Transactional
    //@Async("threadPoolTaskExecutor")
    public void importVoxcoResponse(boolean recreateExtractions) {
        log.info("initiating voxco response extraction. recreateAll={}", recreateExtractions);
        try {
            voxcoDao.clearResultExtractionData();
            createOrStartExtractions(recreateExtractions);
            resultFetchCounter = 0;
            boolean complete = getAndUpdateExtractionResults();
            if (complete) {
                downloadExtractionFiles();
                //TODO: enable later processResponses();
            }
        } catch (Exception e) {
            log.error("Import response encountered error. ", e);
        }
        log.debug("done importing voxco responses");
    }

    private Survey getSurveyByName(List<Survey> surveys, String name) {
        if (surveys != null && !surveys.isEmpty()) {
            Optional<Survey> match = surveys.stream().filter(survey ->
                    name.equals(survey.getName()) && !"RecycleBin".equals(survey.getStatus())).findFirst();
            return match.isPresent() ? match.get() : null;
        }
        return null;
    }

    private Long getMaxSurveyId(List<Survey> surveys) {
        if (surveys != null && !surveys.isEmpty()) {
            surveys.sort(Comparator.comparing(Survey::getId).reversed());
            return surveys.get(0).getId();
        }
        return 0L;
    }

    private Survey createOrUpdateSurvey(Long id, String name, String description) {
        ResponseEntity<Survey> get = surveyClient.getById(id);
        if (HttpStatus.OK.equals(get.getStatusCode())) {
            return surveyClient.update(new Survey(id, name, description)).getBody();
        } else {
            surveyClient.create(new Survey(name, description));
            return surveyClient.getById(id).getBody();
        }
    }

    private void buildAndImportSurvey(ModuleVO module, Survey survey, boolean filter) {
        List<Block> blocks = new ArrayList<>();
        List<List<Choice>> choiceLists = new LinkedList<>();
        choiceId = 0;

        List<String> filteredIdNodes = null;
        if (filter) {
            filteredIdNodes = moduleService.getFilterStudyAgent(module.getIdNode());
            log.debug("Allowed idNodes={}", filteredIdNodes);
        }

        buildBlocksAndChoices(getNodeKey(module.getName()), module.getChildNodes(), blocks, choiceLists,
                filteredIdNodes, null, null, null, false);
        SurveyImportRequest importSurvey = new SurveyImportRequest(survey.getName(), blocks, choiceLists);
        ResponseEntity<SurveyImportResult> response = surveyClient.importSurveyAsJson(importSurvey, survey.getId());
        if (HttpStatus.OK.equals(response.getStatusCode())) {
            log.info("import successful for idNode={} surveyId={}", module.getIdNode(), survey.getId());
        }
    }

    private void buildBlocksAndChoices(String nodeKey, List<QuestionVO> nodeQuestions, List<Block> blocks, List<List<Choice>> choiceLists,
                                       List<String> filteredIdNodes, String parentVariableName, String parentAnswer, String parentNumber, boolean linked) {
        for (QuestionVO question : nodeQuestions) {
            if (!linked && filteredIdNodes != null && !filteredIdNodes.contains(String.valueOf(question.getIdNode()))) {
                log.warn("idNode {} is excluded", question.getIdNode());
                continue;
            }

            if (Constant.Q_FREQUENCY.equals(question.getType())) {
                log.warn("{} with idNode={} will not be included in uploading to Voxco", question.getType(), question.getIdNode());
                continue;
            }

            if (question.getDeleted() == 0 && "Q".equals(question.getNodeclass())) {
                if (Constant.Q_LINKEDAJSM.equals(question.getType()) && question.getLink() != 0L) {
                    NodeVO linkModule = nodeService.getNode(question.getLink());
                    if ("F".equals(linkModule.getNodeclass())) {
                        buildBlocksAndChoices(getNodeKey(linkModule.getName()), ((FragmentVO) linkModule).getChildNodes(),
                                blocks, choiceLists, filteredIdNodes, parentVariableName, parentAnswer, question.getNumber(), true);
                    }
                } else {
                    List<Question> questions = new ArrayList<>();
                    Question.Type type = Question.getType(question.getType());
                    if (type == null) continue; //process single, simple and multiple for now

                    List<Choice> choices = buildChoices(nodeKey, question);
                    log.trace("idNode={}, choiceLists={}, choiceId={}, choices={}",
                            question.getIdNode(), choiceLists.size(), choiceId, choices.size());

                    if (choices.isEmpty()) continue;
                    choiceLists.add(choiceId, choices);

                    String displayLogic = null;
                    if (parentVariableName != null && parentAnswer != null) {
                        displayLogic = LOGIC_BASIC + parentVariableName + LOGIC_EQUAL + parentAnswer;
                    }

                    String questionName = generateName(nodeKey, question.getNumber());
                    String variableName = generateName(type.getVariable(), questionName).toUpperCase();
                    String number = question.getNumber();
                    if (parentNumber != null) {
                        number = generateName(parentNumber, question.getNumber());
                        questionName = generateName(nodeKey, number);
                        variableName = generateName(type.getVariable(), questionName).toUpperCase();
                    }

                    List<Variable> variables = buildVariables(type, variableName, choices.size());
                    questions.add(
                            new Question(questionName.toUpperCase(), type,
                                    new TranslatedTexts(new TranslatedTextContent(question.getName() + "  _" + number)),
                                    variables, displayLogic));
                    blocks.add(new Block(questionName, questions));
                    choiceId++;

                    //build blocks for child questions
                    buildChildBlocksAndChoices(nodeKey, blocks, choiceLists, filteredIdNodes,
                            question.getChildNodes(), variableName, parentNumber, linked);
                }
            }
        }
    }

    private List<Choice> buildChoices(String nodeKey, QuestionVO question) {
        log.trace("building choices for question={}", question.getIdNode());
        List<Choice> choices = new ArrayList<>();
        for(PossibleAnswerVO answer : question.getChildNodes()) {
            if (answer.getDeleted() != 0) continue;

            if ("P".equals(answer.getNodeclass())) {
                String value = generateName(nodeKey, answer.getNumber());
                TranslatedTexts translatedTexts = new TranslatedTexts(new TranslatedTextContent(answer.getName()));
                if ("P_freetext".equals(answer.getType())) {
                    choices.add(new Choice(new ChoiceSettings(), value, translatedTexts));
                } else {
                    choices.add(new Choice(value, translatedTexts));
                }
            }
        }
        return choices;
    }

    private List<Variable> buildVariables(Question.Type type, String variableName, int maxMention) {
        List<Variable> variables = new ArrayList<>();
        Variable variable = new Variable(variableName, choiceId);
        if (Question.Type.CheckBox.equals(type)) {
            variable.setMaxMention(maxMention);
        }
        variables.add(variable);
        return variables;
    }

    private void buildChildBlocksAndChoices(String nodeKey, List<Block> blocks, List<List<Choice>> choiceLists, List<String> filteredIdNodes,
                                            List<PossibleAnswerVO> answers, String variableName, String parentNumber, boolean linked) {
        for (PossibleAnswerVO answer : answers) {
            if (answer.getDeleted() == 0 && "P".equals(answer.getNodeclass())
                    && answer.getChildNodes() != null && !answer.getChildNodes().isEmpty()) {
                buildBlocksAndChoices(nodeKey, answer.getChildNodes(), blocks, choiceLists, filteredIdNodes,
                        variableName, generateName(nodeKey, answer.getNumber()), parentNumber, linked);

            }
        }
    }

    private void createOrStartExtractions(boolean recreateExtractions) {
        List<NodeVoxco> surveys = voxcoDao.getAllActive();
        if (surveys == null || surveys.isEmpty()) return;

        for (NodeVoxco survey : surveys) {
            //create extractions
            if (recreateExtractions || survey.getExtractionId() == null || survey.getExtractionId() == 0L) {
                deleteAndCreateExtraction(survey);
                //implement a retry if no result found?
                SurveyExtractionsResult extraction = resultClient.getSurveyExtractions(survey.getSurveyId()).getBody();
                resultClient.prettyPrint(objectMapper, extraction);
                if (extraction != null && extraction.getExtractions() != null
                        && !extraction.getExtractions().isEmpty()) {
                    extraction.getExtractions().sort(Comparator.comparing(ExtractionResult::getExtractionId).reversed());
                    ExtractionResult result = extraction.getExtractions().get(0);
                    survey.setExtractionId(result.getExtractionId());
                    survey.setExtractionStatus(result.getStatus());
                    survey.setFileId(result.getFileId());
                    survey.setExtractionStart(new Date());
                } else {
                    log.warn("Unable to get any extraction for surveyId={}, surveyName={}",
                            survey.getSurveyId(), survey.getSurveyName());
                }
            } else if (survey.getExtractionId() != null && survey.getExtractionId() > 0L) {
                resultClient.startSurveyExtraction(survey.getExtractionId());
                survey.setExtractionStatus("Triggered");
                survey.setExtractionStart(new Date());
            }
        }
        voxcoDao.updateAll(surveys);
    }

    private void deleteAndCreateExtraction(NodeVoxco survey) {
        if (survey.getExtractionId() != null && survey.getExtractionId() > 0L) {
            resultClient.deleteById(survey.getExtractionId());
        }
        resultClient.create(new Extraction(survey.getSurveyName(), survey.getSurveyId()));
    }

    private boolean getAndUpdateExtractionResults() throws InterruptedException {
        Thread.currentThread().sleep(5000);
        List<NodeVoxco> surveys = voxcoDao.getAllActiveWithPendingExtraction();
        while (!CollectionUtils.isEmpty(surveys) && resultRetryThreshold > resultFetchCounter) {
            for (NodeVoxco survey : surveys) {
                ExtractionResult result = resultClient.getExtractionResult(survey.getExtractionId()).getBody();
                resultClient.prettyPrint(objectMapper,  result);
                survey.setExtractionId(result.getExtractionId());
                survey.setExtractionStatus(result.getStatus());
                survey.setFileId(result.getFileId());
                survey.setExtractionEnd("Completed".equals(result.getStatus()) ? new Date() : null);
            }
            voxcoDao.updateAll(surveys);
            surveys = voxcoDao.getAllActiveWithPendingExtraction();
            resultFetchCounter++;
            Thread.currentThread().sleep(5000);
        }
        return CollectionUtils.isEmpty(surveys);
    }

    private void downloadExtractionFiles() {
        log.debug("start download of extraction files");
        List<NodeVoxco> surveys = voxcoDao.getAllActive();
        if (CollectionUtils.isEmpty(surveys)) return;

        for (NodeVoxco survey : surveys) {
            ResponseEntity<byte[]> response = resultClient.downloadSurveyExtract(
                    survey.getExtractionId(), survey.getFileId());
            try {
                String path = downloadPath + "_" + survey.getSurveyName().toLowerCase();
                Files.deleteIfExists(Paths.get(path));
                FileUtils.writeByteArrayToFile(new File(path), response.getBody());
                File csvFile = extract(path);
                survey.setResultPath(csvFile.getAbsolutePath());
            } catch (IOException io) {
                log.warn("Unable to download and extract file for name={}, extractionId={}, fileId={}",
                        survey.getSurveyName(), survey.getExtractionId(), survey.getFileId());
            }
        }
        voxcoDao.updateAll(surveys);
    }

    private File extract(String path) throws IOException {
        try {
            File compressed = new File(path);
            File extracted = new File(path + "_extract");
            if (!extracted.exists()) {
                extracted.mkdir();
            }
            ZipUtil.unzip(compressed, extracted.getAbsolutePath());
            for (File file : extracted.listFiles()) {
                if ("csv".equals(FilenameUtils.getExtension(file.getName()))) {
                    return file;
                }
            }
        } catch (IOException io) {
            log.error("Unable to extract file.", io);
            throw io;
        }
        return null;
    }

    private Map<String, Map<String, String>> convertToObject(String csvPath) throws IOException {
        List<String[]> extract = CsvUtil.readAll(csvPath);
        Map<String, Map<String, String>> formatted = new LinkedHashMap<>();
        String[] labels = extract.get(0);
        int index = 0;
        for (String[] data : extract) {
            if (index > 0) {
                Map<String, String> entry = new LinkedHashMap<>();
                int dataIndex = 0;
                for (String value : data) {
                    if (dataIndex > 26) {
                        entry.put(labels[dataIndex], value);
                    }
                    dataIndex++;
                }
                formatted.put("CASEID_" + data[0], entry);
            }
            index++;
        }
        return formatted;
    }

    private void processResponses() throws IOException {
        List<NodeVoxco> surveys = voxcoDao.getAllActive();
        if (CollectionUtils.isEmpty(surveys)) return;

        for (NodeVoxco survey : surveys) {
            ModuleVO module = (ModuleVO) moduleService.getNodeById(survey.getIdNode());
            Map<String, Map<String, String>> responses = convertToObject(survey.getResultPath());
            if (CollectionUtils.isEmpty(responses)) continue;

            responses.forEach((key, value) -> {
                ParticipantVO participant = createParticipant(key);
                InterviewVO interview = createInterview(participant);
                //TODO: process answers
                //TODO: where is interview_question inserted?
            });
        }
    }

    private ParticipantVO createParticipant(String reference) {
        ParticipantVO partVO = new ParticipantVO();
        partVO.setReference(reference);
        partVO.setStatus(2);
        return participantService.create(partVO);
    }

    private InterviewVO createInterview(ParticipantVO participant) {
        InterviewVO interview = new InterviewVO();
        interview.setParticipant(participant);
        interview.setReferenceNumber(participant.getReference());
        return interviewService.create(interview);
    }

}
