package org.occideas.qsf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.CommonDataGenerator;
import org.occideas.config.NodeSurveyConfig;
import org.occideas.config.QualtricsConfig;
import org.occideas.entity.JobModule;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.node.dao.NodeDao;
import org.occideas.qsf.*;
import org.occideas.qsf.dao.QSFQuestionMapperDao;
import org.occideas.qsf.payload.SimpleQuestionPayload;
import org.occideas.qsf.response.SurveyCreateResponse;
import org.occideas.qsf.response.SurveyCreateResult;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QSFConversionServiceTest {

    @Mock
    NodeDao nodeDao;
    @Mock
    IQSFClient iqsfClient;
    @Mock
    QualtricsConfig qualtricsConfig;
    @Mock
    QSFQuestionMapperDao qsfQuestionMapperDao;
    @InjectMocks
    QSFConversionService qsfConversionService;

    @Test
    public void givenModule_whenGetOrderedQuestions_thenCreateQuestions() {
        String surveyId = "survey1";
        JobModule introModule = CommonDataGenerator.createModule(2, 2);
        SurveyCreateResponse surveyCreateResponse = new SurveyCreateResponse();
        surveyCreateResponse.setResult(new SurveyCreateResult());
        when(iqsfClient.createQuestion(anyString(), any(SimpleQuestionPayload.class), any())).thenReturn(Response.ok(surveyCreateResponse).build());

        qsfConversionService.createQuestions(introModule.getChildNodes(), surveyId, null, null);

        verify(iqsfClient, times(2)).createQuestion(anyString(), any(SimpleQuestionPayload.class), any());
    }

    @Test
    public void givenModuleAndDeletedQuestion_whenGetOrderedQuestions_thenCreateQuestions() {
        String surveyId = "survey1";
        JobModule introModule = CommonDataGenerator.createModule(2, 2);
        Question question1 = introModule.getChildNodes().get(0);
        question1.setDeleted(1);
        SurveyCreateResponse surveyCreateResponse = new SurveyCreateResponse();
        surveyCreateResponse.setResult(new SurveyCreateResult());
        when(iqsfClient.createQuestion(anyString(), any(SimpleQuestionPayload.class), any())).thenReturn(Response.ok(surveyCreateResponse).build());

        qsfConversionService.createQuestions(introModule.getChildNodes(), surveyId, null, null);

        verify(iqsfClient, times(1)).createQuestion(anyString(), any(SimpleQuestionPayload.class), any());
    }

    @Test
    public void givenModuleMultiAnsQuestionRelationship_whenGetOrderedQuestions_thenCreateQuestions() {
        String surveyId = "survey1";
        JobModule introModule = CommonDataGenerator.createModule(2, 2);
        Question question1 = introModule.getChildNodes().get(0);
        List<PossibleAnswer> possibleAnswersForQuestion1 = CommonDataGenerator.createPossibleAnswers(2, 2);
        question1.setChildNodes(possibleAnswersForQuestion1);
        SurveyCreateResponse surveyCreateResponse = new SurveyCreateResponse();
        surveyCreateResponse.setResult(new SurveyCreateResult());
        when(iqsfClient.createQuestion(anyString(), any(SimpleQuestionPayload.class), any())).thenReturn(Response.ok(surveyCreateResponse).build());

        qsfConversionService.createQuestions(introModule.getChildNodes(), surveyId, null, null);

        verify(iqsfClient, times(6)).createQuestion(anyString(), any(SimpleQuestionPayload.class), any());
    }

    @Test
    public void givenModuleWithLinks_whenGetOrderedQuestions_thenReturnListOfQuestionWrapper() {
        String surveyId = "survey1";
        JobModule introModule = CommonDataGenerator.createModule(2, 2);
        Question question1 = introModule.getChildNodes().get(0);
        List<PossibleAnswer> possibleAnswersForQuestion1 = CommonDataGenerator.createPossibleAnswers(2, 2);
        long linkId = 100L;
        Question parent = possibleAnswersForQuestion1.get(0).getChildNodes().get(0);
        parent.setLink(linkId);
        question1.setChildNodes(possibleAnswersForQuestion1);
        JobModule linkModule = CommonDataGenerator.createModule(2, 2);
        linkModule.getChildNodes().get(0).setIdNode(101L);
        linkModule.getChildNodes().get(1).setIdNode(102L);
        when(nodeDao.getNode(linkId)).thenReturn(linkModule);
        SurveyCreateResponse surveyCreateResponse = new SurveyCreateResponse();
        surveyCreateResponse.setResult(new SurveyCreateResult());
        when(iqsfClient.createQuestion(anyString(), any(SimpleQuestionPayload.class), any())).thenReturn(Response.ok(surveyCreateResponse).build());

        qsfConversionService.createQuestions(introModule.getChildNodes(), surveyId, null, null);

        verify(iqsfClient, times(7)).createQuestion(anyString(), any(SimpleQuestionPayload.class), any());
    }

    @Test
    public void givenQuestionAnswerWrapper_whenBuildQuestionPayload_shouldReturnQuestionPayload() {
        Question question1 = CommonDataGenerator.createQuestionPSimple("1", QSFNodeTypeMapper.Q_SINGLE.getDescription(), 1);

        SimpleQuestionPayload simpleQuestionPayload = qsfConversionService.buildQuestionPayload(new QuestionAnswerWrapper(question1, null));

        assertNotNull(simpleQuestionPayload);
        assertEquals(String.valueOf(question1.getIdNode()), simpleQuestionPayload.getQuestionId());
        assertEquals(question1.getName(), simpleQuestionPayload.getQuestionText());
        assertEquals(QSFQuestionType.SINGLE_CHOICE.getType(), simpleQuestionPayload.getQuestionType());
        assertEquals(QuestionSelector.get(question1.getType()), simpleQuestionPayload.getSelector());
        assertEquals(QSFQuestionSubSelector.TX.name(), simpleQuestionPayload.getSubSelector());
        assertEquals(qsfConversionService.getDefaultConfiguration(), simpleQuestionPayload.getConfiguration());
        assertEquals(question1.getName(), simpleQuestionPayload.getQuestionDescription());
        assertNotNull(simpleQuestionPayload.getChoices());
        assertFalse(simpleQuestionPayload.getChoices().isEmpty());
        assertNotNull(simpleQuestionPayload.getChoiceOrderList());
        assertFalse(simpleQuestionPayload.getChoiceOrderList().length == 0);
        assertNotNull(simpleQuestionPayload.getValidation());
        assertNotNull(simpleQuestionPayload.getLanguageList());
        assertNotNull(simpleQuestionPayload.getDataExportTag());
        assertNull(simpleQuestionPayload.getLogic());
    }

    @Test
    public void givenQuestionAnswerWrapper_whenBuildQuestionPayload_shouldReturnQuestionPayloadWithDisplayLogic() {
        Question question1 = CommonDataGenerator.createQuestionPSimple("1", QSFNodeTypeMapper.Q_MULTIPLE.getDescription(), 1);
        PossibleAnswer answer = CommonDataGenerator.createPossibleAnswer("1a", "P_simple");
        QuestionAnswerWrapper questionAnswerWrapper = new QuestionAnswerWrapper(question1, answer);
        questionAnswerWrapper.setParent(new QuestionAnswerWrapper(CommonDataGenerator.createQuestionPSimple("0", QSFNodeTypeMapper.Q_MULTIPLE.getDescription(), 1), null));

        SimpleQuestionPayload simpleQuestionPayload = qsfConversionService.buildQuestionPayload(questionAnswerWrapper);

        assertNotNull(simpleQuestionPayload);
        assertEquals(String.valueOf(question1.getIdNode()), simpleQuestionPayload.getQuestionId());
        assertEquals(question1.getName(), simpleQuestionPayload.getQuestionText());
        assertNotNull(simpleQuestionPayload.getLogic());
    }

    @Test
    void givenDifferentTypesOfAnswers_whenGetQuestionTypeBaseOnAnswers_shouldReturnCorrectType() {
        Question multiAnswerQuestion = CommonDataGenerator.createQuestionPSimple("1q", QSFNodeTypeMapper.Q_MULTIPLE.getDescription(), 2);
        Question singleAnswerQuestion = CommonDataGenerator.createQuestionPSimple("1q", QSFNodeTypeMapper.Q_SINGLE.getDescription(), 2);
        Question freetextAnswerQuestion = CommonDataGenerator.createQuestionPFreetext("1q", QSFNodeTypeMapper.Q_SINGLE.getDescription(), 1);
        freetextAnswerQuestion.getChildNodes().get(0).setType(QSFNodeTypeMapper.P_FREETEXT.getDescription());
        Question frequencyAnswerQuestion = CommonDataGenerator.createQuestionPSimple("1q", QSFNodeTypeMapper.Q_FREQUENCY.getDescription(), 1);
        frequencyAnswerQuestion.getChildNodes().get(0).setType(QSFNodeTypeMapper.P_FREQUENCY_HOURS.getDescription());

        QSFQuestionType singleChoice = qsfConversionService.getQuestionTypeBaseOnAnswers(singleAnswerQuestion);
        QSFQuestionType multiChoice = qsfConversionService.getQuestionTypeBaseOnAnswers(multiAnswerQuestion);
        QSFQuestionType freetext = qsfConversionService.getQuestionTypeBaseOnAnswers(freetextAnswerQuestion);
        QSFQuestionType frequency = qsfConversionService.getQuestionTypeBaseOnAnswers(frequencyAnswerQuestion);

        assertEquals(QSFQuestionType.TEXT_ENTRY, freetext);
        assertEquals(QSFQuestionType.SINGLE_CHOICE, singleChoice);
        assertEquals(QSFQuestionType.MULTIPLE_CHOICE, multiChoice);
        assertEquals(QSFQuestionType.TEXT_ENTRY_FORM, frequency);
    }

    @Test
    public void givenModuleWithFilters_whenGetOrderedQuestions_thenCreateQuestions() {
        String surveyId = "survey1";
        JobModule introModule = CommonDataGenerator.createModule(2, 2);
        List<String> filterIds = new ArrayList<>();
        filterIds.add(String.valueOf(introModule.getChildNodes().get(0).getIdNode()));
        SurveyCreateResponse surveyCreateResponse = new SurveyCreateResponse();
        surveyCreateResponse.setResult(new SurveyCreateResult());
        qualtricsConfig.setNode(new NodeSurveyConfig());
        when(iqsfClient.createQuestion(anyString(), any(SimpleQuestionPayload.class), any())).thenReturn(Response.ok(surveyCreateResponse).build());

        qsfConversionService.createQuestions(filterIds, introModule.getChildNodes(), surveyId, null, null);

        verify(iqsfClient, times(1)).createQuestion(anyString(), any(SimpleQuestionPayload.class), any());
    }


}