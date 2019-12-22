package org.occideas.qsf.dao;

public interface INodeQSFDao {

    String save(String surveyId,long idNode);

    String getByIdNode(long idNode);

}
