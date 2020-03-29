package org.occideas.qsf.service;

import org.occideas.entity.NodeQSF;
import org.occideas.qsf.results.SurveyResponses;
import org.springframework.scheduling.annotation.Async;

public interface IQSFService {

    String save(String surveyId, long idNode, String path);

    String getSurveyIdByIdNode(long idNode);

    NodeQSF getByIdNode(long idNode);

    @Async("threadPoolTaskExecutor")
    void exportResponseQSF(Long id) throws InterruptedException;

    SurveyResponses exportQSFResponses(long idNode) throws InterruptedException;

    void consumeQSFResponse(SurveyResponses surveyResponses);

    @Async("threadPoolTaskExecutor")
    void importQSFResponses();

    @Async("threadPoolTaskExecutor")
    void importQSFResponsesForIntro();
}
