package org.occideas.voxco.dao;

import org.occideas.entity.NodeVoxco;

import java.util.List;

public interface INodeVoxcoDao {

    Long save(Long surveyId, long idNode, String surveyName);

    Long getMaxSurveyId();

    List<NodeVoxco> findByIdNodeAndDeleted(Long idNode, Boolean deleted);

}
