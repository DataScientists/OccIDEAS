package org.occideas.qsf.service;

import org.occideas.entity.NodeQSF;
import org.occideas.qsf.response.Response;
import org.occideas.qsf.response.SurveyResponses;
import org.occideas.vo.NodeVO;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

public interface IQSFService {

    String save(String surveyId, long idNode, String path);

    String getSurveyIdByIdNode(long idNode);

    NodeQSF getByIdNode(long idNode);

    @Async("threadPoolTaskExecutor")
    void copySurveys(String userId, String prefix) throws InterruptedException;

    @Async("threadPoolTaskExecutor")
    void exportResponseQSF(Long id) throws InterruptedException;

    void importResponseQSF(String surveyId) throws InterruptedException, IOException;

    SurveyResponses exportQSFResponses(long idNode) throws InterruptedException;

    void consumeQSFResponse(SurveyResponses surveyResponses);

    @Async("threadPoolTaskExecutor")
    void importQSFResponses();

    @Async("threadPoolTaskExecutor")
    void importQSFResponsesForIntro();

    void cleanSurveyResponses();

    void processResponse(NodeVO nodeVO, Response response);

    void processResponse(Response response);

    void createQSFTranslationModule();
}
