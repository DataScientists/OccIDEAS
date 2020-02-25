package org.occideas.qsf.service;

public interface IQSFService {

    String save(String surveyId, long idNode, String path);

    String getByIdNode(long idNode);
}
