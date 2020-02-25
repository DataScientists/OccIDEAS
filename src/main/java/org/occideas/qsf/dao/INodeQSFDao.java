package org.occideas.qsf.dao;

public interface INodeQSFDao {

    String save(String surveyId, long idNode, String results);

    String getByIdNode(long idNode);

}
