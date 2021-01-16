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
import java.util.List;

@Service
public class VoxcoService implements IVoxcoService {
    private static final Logger log = LogManager.getLogger(VoxcoService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private INodeVoxcoDao voxcoDao;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private IVoxcoClient<Survey, Long> iVoxcoClient;

    private String generateName(String name, Long key) {
        return name + "_" + key;
    }

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
                String generatedName = generateName(module.getName(), surveyId);
                name = generatedName.equals(voxco) ? voxco.getSurveyName() : generatedName;
            } else {
                surveyId = voxcoDao.getMaxSurveyId() + 1;
                name = generateName(module.getName(), surveyId);
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
        List<List<Choice>> choiceLists = new ArrayList<>();
        buildBlocksAndChoices(module, blocks, choiceLists);
        SurveyImportRequest importSurvey = new SurveyImportRequest(survey.getName(), blocks, choiceLists);
        ResponseEntity<SurveyImportResult> response = iVoxcoClient.importSurveyAsJson(importSurvey, survey.getId());
        if (HttpStatus.OK.equals(response.getStatusCode())) {
            log.info("import successful for idNode={}", module.getIdNode());
        }
    }

    private void buildBlocksAndChoices(ModuleVO module, List<Block> blocks, List<List<Choice>> choiceLists) {
        int choiceId = 0;
        for (QuestionVO question : module.getChildNodes()) {
            List<Question> questions = new ArrayList<>();
            if (question.getDeleted() == 0) {
                List<Choice> choices = new ArrayList<>();
                for(PossibleAnswerVO answer : question.getChildNodes()) {
                    choices.add(new Choice(answer.getName(),
                            new TranslatedTexts(new TranslatedTextContent(answer.getName()))));
                }
                choiceLists.add(choiceId, choices);
                String questionName = generateName(question.getNodeType(), Long.valueOf(question.getNumber()));
                Question.Type type = Question.getType(question.getType());
                List<Variable> variables = new ArrayList<>();
                String variableName =  generateName(type.getVariable(), Long.valueOf(question.getNumber()));
                Variable variable = new Variable(variableName, choiceId);
                if (Question.Type.CheckBox.equals(type)) {
                    variable.setMaxMention(choices.size());
                }
                variables.add(variable);
                questions.add(
                        new Question(questionName, type,
                                new TranslatedTexts(new TranslatedTextContent(question.getName())),
                                variables));
                blocks.add(new Block(questionName, questions));
                choiceId++;
            }
        }
    }
}
