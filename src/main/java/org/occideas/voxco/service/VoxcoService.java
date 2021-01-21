package org.occideas.voxco.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.Constant;
import org.occideas.entity.NodeVoxco;
import org.occideas.module.service.ModuleService;
import org.occideas.node.service.INodeService;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.voxco.dao.INodeVoxcoDao;
import org.occideas.voxco.model.Block;
import org.occideas.voxco.model.Choice;
import org.occideas.voxco.model.ChoiceSettings;
import org.occideas.voxco.model.Question;
import org.occideas.voxco.model.Survey;
import org.occideas.voxco.model.TranslatedTextContent;
import org.occideas.voxco.model.TranslatedTexts;
import org.occideas.voxco.model.User;
import org.occideas.voxco.model.Variable;
import org.occideas.voxco.request.SurveyImportRequest;
import org.occideas.voxco.response.SurveyImportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class VoxcoService implements IVoxcoService {
    private static final Logger log = LogManager.getLogger(VoxcoService.class);

    private static final String LOGIC_BASIC = "logic:basic;";
    private static final String LOGIC_NOT_EQUAL = " != ";
    private static final String LOGIC_EQUAL = " = ";

    @Autowired
    private INodeVoxcoDao voxcoDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private INodeService nodeService;

    @Autowired
    private IVoxcoClient<Survey, Long> surveyClient;

    @Autowired
    private IVoxcoClient<User, Long> userClient;

    private int choiceId;

    private String generateName(String name, String key) {
        return name + "_" + key;
    }

    private String getNodeKey(String nodeName) {
        return nodeName.substring(0, 4);
    }

    @Override
    @Transactional
    public void importSurvey(Long id) {
        importSurvey(id, true);
    }

    @Override
    @Transactional
    public void importSurvey(Long id, boolean filter) {
        List<ModuleVO> modules = moduleService.findById(id);
        if (!modules.isEmpty()) {
            ModuleVO module = modules.get(0);
            List<Survey> surveys = userClient.getUserSurveys().getBody();
            Long surveyId = getMaxSurveyId(surveys) + 1;
            String name = generateName(module.getName(), String.valueOf(surveyId));

            List<NodeVoxco> voxcoList = voxcoDao.findByIdNodeAndDeleted(module.getIdNode(), Boolean.FALSE);
            if (voxcoList != null && !voxcoList.isEmpty()) {
                NodeVoxco voxco = voxcoList.get(0);
                Survey matched = getSurveyByName(surveys, voxco.getSurveyName());
                if (matched != null && matched.getId().equals(voxco.getSurveyId())) {
                    surveyId = matched.getId();
                    name = matched.getName();
                }
            }

            Survey survey = createOrUpdateSurvey(surveyId, name, module.getDescription());
            voxcoDao.save(surveyId, module.getIdNode(), survey.getName());
            buildAndImportSurvey(module, survey, filter);
        }
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
                filteredIdNodes, null, null, null);
        SurveyImportRequest importSurvey = new SurveyImportRequest(survey.getName(), blocks, choiceLists);
        ResponseEntity<SurveyImportResult> response = surveyClient.importSurveyAsJson(importSurvey, survey.getId());
        if (HttpStatus.OK.equals(response.getStatusCode())) {
            log.info("import successful for idNode={}", module.getIdNode());
        }
    }

    private void buildBlocksAndChoices(String nodeKey, List<QuestionVO> nodeQuestions, List<Block> blocks, List<List<Choice>> choiceLists,
                                       List<String> filteredIdNodes, String parentVariableName, String parentAnswer, String parentNumber) {
        for (QuestionVO question : nodeQuestions) {
            if (filteredIdNodes != null && !filteredIdNodes.contains(String.valueOf(question.getIdNode()))) {
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
                                blocks, choiceLists, filteredIdNodes, parentVariableName, parentAnswer, question.getNumber());
                    }
                } else {
                    List<Question> questions = new ArrayList<>();
                    Question.Type type = Question.getType(question.getType());
                    if (type == null) continue; //process single, simple and multiple for now

                    List<Choice> choices = buildChoices(nodeKey, question);
                    if (!choices.isEmpty()) choiceLists.add(choiceId, choices);

                    String displayLogic = null;
                    if (parentVariableName != null && parentAnswer != null) {
                        displayLogic = LOGIC_BASIC + parentVariableName + LOGIC_EQUAL + parentAnswer;
                    }

                    String questionName = generateName(nodeKey, question.getNumber());
                    String variableName = generateName(type.getVariable(), question.getNumber());
                    if (parentNumber != null) {
                        questionName = generateName(nodeKey, generateName(parentNumber, question.getNumber()));
                        variableName = generateName(type.getVariable(), generateName(parentNumber, question.getNumber()));
                    }

                    List<Variable> variables = buildVariables(type, variableName, choices.size());
                    questions.add(
                            new Question(questionName.toUpperCase(), type,
                                    new TranslatedTexts(new TranslatedTextContent(question.getName())),
                                    variables, displayLogic));
                    blocks.add(new Block(questionName, questions));
                    choiceId++;

                    //build blocks for child questions
                    buildChildBlocksAndChoices(nodeKey, blocks, choiceLists, filteredIdNodes,
                            question.getChildNodes(), variableName, parentNumber);
                }
            }
        }
    }

    private List<Choice> buildChoices(String nodeKey, QuestionVO question) {
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
                                            List<PossibleAnswerVO> answers, String variableName, String parentNumber) {
        for (PossibleAnswerVO answer : answers) {
            if (answer.getDeleted() == 0 && "P".equals(answer.getNodeclass())
                    && answer.getChildNodes() != null && !answer.getChildNodes().isEmpty()) {
                buildBlocksAndChoices(nodeKey, answer.getChildNodes(), blocks, choiceLists, filteredIdNodes,
                        variableName, generateName(nodeKey, answer.getNumber()), parentNumber);

            }
        }
    }
}
