package org.occideas.qsf.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.config.QualtricsConfig;
import org.occideas.entity.*;
import org.occideas.exceptions.GenericException;
import org.occideas.module.service.ModuleService;
import org.occideas.node.dao.NodeDao;
import org.occideas.qsf.*;
import org.occideas.qsf.dao.NodeQSFDao;
import org.occideas.qsf.dao.QSFQuestionMapperDao;
import org.occideas.qsf.payload.*;
import org.occideas.qsf.request.SurveyCreateRequest;
import org.occideas.qsf.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@Service
public class QSFConversionService {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private NodeDao nodeDao;
    @Autowired
    private IQSFClient iqsfClient;
    @Autowired
    private NodeQSFDao nodeQSFDao;
    @Autowired
    @Lazy
    private ModuleService moduleService;
    @Autowired
    private QualtricsConfig qualtricsConfig;
    @Autowired
    private QSFQuestionMapperDao qsfQuestionMapperDao;

    private final Configuration configuration = new Configuration("UseText");

    public String uploadQSF(JobModule jobModule, List<String> filterIds) {
        SurveyCreateResult surveyCreateResult = createSurvey(jobModule, jobModule.getName());
        if (Objects.isNull(surveyCreateResult)) {
            String message = "Error: SurveyCreateResult is null from qualtrics survey during creation of survey";
            log.error(message);
            throw new GenericException(message);
        }
        String surveyId = surveyCreateResult.getSurveyId();
        qsfQuestionMapperDao.deleteBySurveyId(surveyId);
        String blockId = surveyCreateResult.getDefaultBlockId();
        if (Objects.isNull(filterIds) || filterIds.isEmpty()) {
            createQuestions(jobModule.getChildNodes(), surveyId, null, null);
        } else {
            createQuestions(filterIds, jobModule.getChildNodes(), surveyId, null, null);
        }

        GetBlockElementResult getBlockElementResult = getBlock(surveyId, blockId);
        createPageBreaks(getBlockElementResult);
        iqsfClient.updateBlock(surveyId, blockId, getBlockElementResult);
        final Response surveyOptions = iqsfClient.getSurveyOptions(surveyId);
        SurveyOptionResponse options = (SurveyOptionResponse) surveyOptions.getEntity();
        options.getResult().setBackButton("true");
        options.getResult().setEosRedirectURL(qualtricsConfig.getRedirectUrl());
        options.getResult().setSurveyTermination("EOSRedirectURL");
        iqsfClient.updateSurveyOptions(surveyId, options.getResult());
        iqsfClient.publishSurvey(surveyId);
        iqsfClient.activateSurvey(surveyId);
        return surveyId;
    }

    public void createQuestions(List<String> filterIds,
                                List<Question> questions,
                                String surveyId,
                                PossibleAnswer dependsOn,
                                QuestionAnswerWrapper parent) {
        questions.stream()
                .filter(question -> filterIds.contains(String.valueOf(question.getIdNode())) ||
                        ignoreEmailNode(question))
                .filter(question -> question.getDeleted() == 0)
                .forEach(question -> {
                    if (question.getLink() > 0) {
                        Node node = nodeDao.getNode(question.getLink());
                        createQuestions(moduleService.getFilterStudyAgent(question.getLink()), node.getChildNodes(), surveyId, dependsOn, parent);
                    } else {
                        QuestionAnswerWrapper questionAnswerWrapper = new QuestionAnswerWrapper(question, dependsOn);
                        questionAnswerWrapper.setParent(parent);

                        SimpleQuestionPayload questionPayload = buildQuestionPayload(questionAnswerWrapper);
                        Response responseQuestion = iqsfClient.createQuestion(surveyId, questionPayload, null);

                        if (!question.getChildNodes().isEmpty()) {
                            Long freetextIdNode = null;
                            if (QSFNodeTypeMapper.Q_FREQUENCY.getDescription().equalsIgnoreCase(question.getType())) {
                                freetextIdNode = question.getChildNodes().get(0).getIdNode();
                            }

                            qsfQuestionMapperDao.save(new QSFQuestionMapper(new QSFQuestionMapperId(surveyId, questionPayload.getQuestionId()), question.getIdNode(), question.getType(), question.getName(), freetextIdNode));
                            SurveyCreateResponse surveyCreateResponse = (SurveyCreateResponse) responseQuestion.getEntity();
                            question.getChildNodes()
                                    .stream()
                                    .filter(possibleAnswer -> possibleAnswer.getDeleted() == 0)
                                    .forEach(possibleAnswer -> {
                                        if (isChoicesRequired(possibleAnswer)) {
                                            questionAnswerWrapper.getChoiceSelectors().put(possibleAnswer, "q://" + surveyCreateResponse.getResult().getQuestionId() + "/SelectableChoice/" + possibleAnswer.getIdNode());
                                            if (Objects.nonNull(possibleAnswer.getChildNodes())) {
                                                createQuestions(filterIds, possibleAnswer.getChildNodes(), surveyId, possibleAnswer, questionAnswerWrapper);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private boolean ignoreEmailNode(Question question) {
        if (Objects.isNull(qualtricsConfig.getNode())) {
            return false;
        }
        return String.valueOf(question.getIdNode()).equalsIgnoreCase(qualtricsConfig.getNode().getEmail());
    }

    public void createQuestions(List<Question> questions, String surveyId, PossibleAnswer dependsOn, QuestionAnswerWrapper parent) {
        questions.stream().filter(question -> question.getDeleted() == 0)
                .forEach(question -> {
                    if (question.getLink() > 0) {
                        Node node = nodeDao.getNode(question.getLink());
                        createQuestions(node.getChildNodes(), surveyId, dependsOn, parent);
                    } else {
                        QuestionAnswerWrapper questionAnswerWrapper = new QuestionAnswerWrapper(question, dependsOn);
                        questionAnswerWrapper.setParent(parent);

                        SimpleQuestionPayload questionPayload = buildQuestionPayload(questionAnswerWrapper);
                        Response responseQuestion = iqsfClient.createQuestion(surveyId, questionPayload, null);
                        SurveyCreateResponse surveyCreateResponse = (SurveyCreateResponse) responseQuestion.getEntity();
                        if (!question.getChildNodes().isEmpty()) {
                            Long freetextIdNode = null;
                            if (QSFNodeTypeMapper.Q_FREQUENCY.getDescription().equalsIgnoreCase(question.getType())) {
                                freetextIdNode = question.getChildNodes().get(0).getIdNode();
                            }
                            qsfQuestionMapperDao.save(new QSFQuestionMapper(new QSFQuestionMapperId(surveyId, surveyCreateResponse.getResult().getQuestionId()), question.getIdNode(), question.getType(), question.getName(), freetextIdNode));
                            question.getChildNodes()
                                    .stream()
                                    .filter(possibleAnswer -> possibleAnswer.getDeleted() == 0)
                                    .forEach(possibleAnswer -> {
                                        if (isChoicesRequired(possibleAnswer)) {
                                            questionAnswerWrapper.getChoiceSelectors().put(possibleAnswer, "q://" + surveyCreateResponse.getResult().getQuestionId() + "/SelectableChoice/" + possibleAnswer.getIdNode());
                                            if (Objects.nonNull(possibleAnswer.getChildNodes())) {
                                                createQuestions(possibleAnswer.getChildNodes(), surveyId, possibleAnswer, questionAnswerWrapper);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private boolean isChoicesRequired(PossibleAnswer possibleAnswer) {
        return !QSFNodeTypeMapper.P_FREETEXT.getDescription().equalsIgnoreCase(possibleAnswer.getType());
    }

    public SimpleQuestionPayload buildQuestionPayload(QuestionAnswerWrapper questionAnswerWrapper) {
        Question question = questionAnswerWrapper.getQuestion();
        if (Objects.isNull(question.getChildNodes()) || question.getChildNodes().isEmpty()) {
            throw new GenericException("Question with child nodes are not allowed " + question.getIdNode());
        }
        SimpleQuestionPayload simpleQuestionPayload = new SimpleQuestionPayload();
        simpleQuestionPayload.setQuestionId(String.valueOf(question.getIdNode()));
        simpleQuestionPayload.setQuestionText(question.getName());
        QSFQuestionType questionTypeBaseOnAnswers = getQuestionTypeBaseOnAnswers(question);
        simpleQuestionPayload.setQuestionType(questionTypeBaseOnAnswers.getType());
        simpleQuestionPayload.setSelector(questionTypeBaseOnAnswers.getSelector());
        simpleQuestionPayload.setSubSelector(questionTypeBaseOnAnswers.getSubSelector());
        simpleQuestionPayload.setConfiguration(getDefaultConfiguration());
        simpleQuestionPayload.setQuestionDescription(question.getName());
        simpleQuestionPayload.setDataExportTag(question.getIdNode() + "-" + question.getNumber());
        QSFQuestionChoices derivedChoices = QSFNodeTypeMapper.getBaseOnType(question.getChildNodes().get(0).getType()).getBuildChoices().apply(question);
        simpleQuestionPayload.setChoices(derivedChoices.getChoices());
        simpleQuestionPayload.setChoiceOrderList(derivedChoices.getChoiceOrder().toArray(new String[0]));
        simpleQuestionPayload.setLanguageList(new ArrayList<>());
        simpleQuestionPayload.setValidation(new Validation(new Setting("ON", "ON", "None")));
        if (Objects.nonNull(questionAnswerWrapper.getDependsOn())) {
            List<Logic> logics = QSFNodeTypeMapper.getBaseOnType(questionAnswerWrapper.getQuestion().getType()).getBuildLogic().apply(questionAnswerWrapper);
            simpleQuestionPayload.setLogic(new DisplayLogic("BooleanExpression", false,
                    new Condition(buildLogicMap(logics), "If")));
        }
        return simpleQuestionPayload;
    }

    protected QSFQuestionType getQuestionTypeBaseOnAnswers(Question question) {
        if (!question.getChildNodes().isEmpty()) {
            String type = question.getChildNodes().get(0).getType();
            if (StringUtils.isEmpty(type)) {
                return QSFQuestionType.MULTIPLE_CHOICE;
            }
            return QSFNodeTypeMapper.getBaseOnType(type).getQualtricsType();
        }
        return QSFQuestionType.MULTIPLE_CHOICE;
    }

    private Map<Integer, Logic> buildLogicMap(List<Logic> list) {
        Map<Integer, Logic> map = new HashMap<>();
        AtomicInteger count = new AtomicInteger(0);
        list.forEach(logic -> map.put(count.getAndIncrement(), logic));
        return map;
    }

    public Configuration getDefaultConfiguration() {
        return configuration;
    }

    private SurveyCreateResult createSurvey(JobModule moduleVO, String surveyName) {
        Response response = iqsfClient.createSurvey(new SurveyCreateRequest(
                StringUtils.isNotBlank(surveyName) ? surveyName : moduleVO.getName().replaceAll("\\s+", ""),
                "EN",
                "CORE"
        ));
        Object entity = response.getEntity();
        if (entity instanceof SurveyCreateResponse) {
            SurveyCreateResponse surveyCreateResponse = (SurveyCreateResponse) entity;
            nodeQSFDao.save(surveyCreateResponse.getResult().getSurveyId(), moduleVO.getIdNode(), null);
            return surveyCreateResponse.getResult();
        }
        return null;
    }

    private GetBlockElementResult getBlock(String surveyId, String blockId) {
        Response response = iqsfClient.getBlock(surveyId, blockId);
        Object entity = response.getEntity();
        if (entity instanceof GetBlockElementResponse) {
            GetBlockElementResponse getBlockElementResponse = (GetBlockElementResponse) entity;
            return getBlockElementResponse.getResult();
        }
        return null;
    }

    private void createPageBreaks(GetBlockElementResult getBlockElementResult) {
        int size = getBlockElementResult.getBlockElement().size();
        int x = 0;
        for (int i = 0; i < size; i++) {
            getBlockElementResult.getBlockElement().add(x + 1, new BlockElement("Page Break", null));
            x += 2;
        }
    }
}
