package org.occideas.voxco.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.NodeVoxco;
import org.occideas.module.service.ModuleService;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.voxco.dao.INodeVoxcoDao;
import org.occideas.voxco.model.Block;
import org.occideas.voxco.model.Choice;
import org.occideas.voxco.model.Question;
import org.occideas.voxco.model.Survey;
import org.occideas.voxco.model.TranslatedTextContent;
import org.occideas.voxco.model.TranslatedTexts;
import org.occideas.voxco.model.Variable;
import org.occideas.voxco.request.SurveyImportRequest;
import org.occideas.voxco.response.SurveyImportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class VoxcoService implements IVoxcoService {
    private static final Logger log = LogManager.getLogger(VoxcoService.class);

    private static final String SKIP_LOGIC_PRE = "logic:basic;";
    private static final String SKIP_LOGIC_NOT_EQUAL = " != ";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private INodeVoxcoDao voxcoDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private IVoxcoClient<Survey, Long> iVoxcoClient;

    private String generateName(String name, String key) {
        return name + "_" + key;
    }

    private int choiceId;

    @Override
    @Transactional
    public void importSurvey(Long id) {
        List<ModuleVO> modules = moduleService.findById(id);
        if (!modules.isEmpty()) {
            ModuleVO module = modules.get(0);
            Long surveyId;
            String name;
            List<NodeVoxco> voxcoList = voxcoDao.findByIdNodeAndDeleted(module.getIdNode(), Boolean.FALSE);
            if (voxcoList != null && !voxcoList.isEmpty()) {
                NodeVoxco voxco = voxcoList.get(0);
                surveyId = voxco.getSurveyId();
                String generatedName = generateName(module.getName(), String.valueOf(surveyId));
                name = generatedName.equals(voxco.getSurveyName()) ? voxco.getSurveyName() : generatedName;
            } else {
                surveyId = voxcoDao.getMaxSurveyId() + 1;
                name = generateName(module.getName(), String.valueOf(surveyId));
            }
            Survey survey = createOrUpdateSurvey(surveyId, name, module.getDescription());
            voxcoDao.save(surveyId, module.getIdNode(), survey.getName());
            buildAndImportSurvey(module, survey);
        }
    }

    private Survey createOrUpdateSurvey(Long id, String name, String description) {
        ResponseEntity<Survey> get = iVoxcoClient.getById(id);
        if (HttpStatus.OK.equals(get.getStatusCode())) {
            return iVoxcoClient.update(new Survey(id, name, description)).getBody();
        } else {
            iVoxcoClient.create(new Survey(name, description));
            return iVoxcoClient.getById(id).getBody();
        }
    }

    private void buildAndImportSurvey(ModuleVO module, Survey survey) {
        List<Block> blocks = new ArrayList<>();
        List<List<Choice>> choiceLists = new LinkedList<>();
        choiceId = 0;
        buildBlocksAndChoices(module.getChildNodes(), blocks, choiceLists, null);
        SurveyImportRequest importSurvey = new SurveyImportRequest(survey.getName(), blocks, choiceLists);
        ResponseEntity<SurveyImportResult> response = iVoxcoClient.importSurveyAsJson(importSurvey, survey.getId());
        if (HttpStatus.OK.equals(response.getStatusCode())) {
            log.info("import successful for idNode={}", module.getIdNode());
        }
    }

    private void buildBlocksAndChoices(List<QuestionVO> nodeQuestions, List<Block> blocks, List<List<Choice>> choiceLists, String skipLogic) {
        for (QuestionVO question : nodeQuestions) {
            List<Question> questions = new ArrayList<>();
            if (question.getDeleted() == 0 && "Q".equals(question.getNodeclass())) {
                Question.Type type = Question.getType(question.getType());
                if (type == null) continue; //process single, simple and multiple for now

                List<Choice> choices = buildChoices(question);
                if (!choices.isEmpty()) choiceLists.add(choiceId, choices);

                String questionName = generateName(question.getNodeType(), question.getNumber());
                List<Variable> variables = new ArrayList<>();
                String variableName =  generateName(type.getVariable(), question.getNumber());
                Variable variable = new Variable(variableName, choiceId);
                if (Question.Type.CheckBox.equals(type)) {
                    variable.setMaxMention(choices.size());
                }
                variables.add(variable);
                questions.add(
                        new Question(questionName, type,
                                new TranslatedTexts(new TranslatedTextContent(question.getName())),
                                variables));
                blocks.add(new Block(questionName, questions, skipLogic));
                choiceId++;

                //build blocks for child questions
                for(PossibleAnswerVO answer : question.getChildNodes()) {
                    if (answer.getDeleted() == 0 && "P".equals(answer.getNodeclass())
                            && answer.getChildNodes() != null && !answer.getChildNodes().isEmpty()) {
                        skipLogic = SKIP_LOGIC_PRE + variableName + SKIP_LOGIC_NOT_EQUAL + answer.getName();
                        buildBlocksAndChoices(answer.getChildNodes(), blocks, choiceLists, skipLogic);
                    }
                }
            }
        }
    }

    private List<Choice> buildChoices(QuestionVO question) {
        List<Choice> choices = new ArrayList<>();
        for(PossibleAnswerVO answer : question.getChildNodes()) {
            if (answer.getDeleted() == 0 && "P".equals(answer.getNodeclass())) {
                choices.add(new Choice(answer.getName(),
                        new TranslatedTexts(new TranslatedTextContent(answer.getName()))));
            }
        }
        return choices;
    }
}
