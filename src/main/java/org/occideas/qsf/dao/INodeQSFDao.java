package org.occideas.qsf.dao;

import org.occideas.entity.NodeQSF;

import java.util.List;

public interface INodeQSFDao {

    String save(String surveyId, long idNode, String results);

    String getByIdNode(long idNode);

    List<NodeQSF> list();
}
