package org.occideas.voxco.dao;

import org.occideas.entity.NodeVoxco;

import java.util.Date;
import java.util.List;

public interface INodeVoxcoDao {

    Long save(Long surveyId, long idNode, String surveyName);

    void updateAll(List<NodeVoxco> surveys);

    void update(long surveyId, long idNode, Long extractionId, String extractionStatus,
                Long fileId, Date extractionStart, Date extractionEnd, String resultPath,
                Integer importFilterCount, Integer importQuestionCount, Integer voxcoQuestionCount,
                Date lastValidated);

    Long getMaxSurveyId();

    List<NodeVoxco> findByIdNodeAndDeleted(Long idNode, Boolean deleted);

    List<NodeVoxco> getAllActive();

    List<NodeVoxco> getAllActiveWithPendingExtraction();

    void clearResultExtractionData();
}
