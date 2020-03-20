package org.occideas.qsf.service;

import org.occideas.qsf.results.SurveyResponses;

public interface IQSFService {

    String save(String surveyId, long idNode, String path);

    String getByIdNode(long idNode);

    void consumeQSFResponse(SurveyResponses surveyResponses);

    void importQSFResponses();
}
