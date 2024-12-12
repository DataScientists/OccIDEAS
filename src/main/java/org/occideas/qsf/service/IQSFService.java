package org.occideas.qsf.service;

import org.occideas.entity.Interview;
import org.occideas.entity.NodeQSF;
import org.occideas.qsf.response.Response;
import org.occideas.qsf.response.SurveyResponses;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.ResponseSummary;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IQSFService {

    String save(String surveyId, long idNode, String path);

    String getSurveyIdByIdNode(long idNode);

    NodeQSF getByIdNode(long idNode);

    @Async("threadPoolTaskExecutor")
    void copySurveys(String userId, String prefix) throws InterruptedException;

    @Async("threadPoolTaskExecutor")
    void exportResponseQSF(Long id) throws InterruptedException;

    void importResponseQSF(String surveyId, String reference) throws InterruptedException, IOException;

    SurveyResponses exportQSFResponses(long idNode) throws InterruptedException;

    void consumeQSFResponse(SurveyResponses surveyResponses);

    void processResponseAnswers(Response response, String referenceNumber, InterviewVO newInterview, NodeVO nodeVO);

    @Async("threadPoolTaskExecutor")
    void importQSFResponses();

    @Async("threadPoolTaskExecutor")
    void importQSFResponsesForIntro();

    void cleanSurveyResponses();

    void processResponse(NodeVO nodeVO, Response response);

    long processResponse(String surveyId, String reference,Response response);

    String getWorkshift(Interview interview);

    Long getWorkshiftIdNode(Interview interview);

    void saveAssessmentResults(String referenceNumber,
                               List<Long> listAgentIds,
                               Interview interview,
                               BigDecimal workshift);

    void saveQualtricsResponse(String surveyId,
                               String responseId,
                               Map<String, Object> values,
                               Map<String, ResponseSummary> summary);

    void createQSFTranslationModule();
}
