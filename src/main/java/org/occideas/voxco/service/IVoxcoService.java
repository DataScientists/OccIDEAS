package org.occideas.voxco.service;

import org.occideas.vo.NodeVoxcoVO;

import java.util.List;

public interface IVoxcoService {

    void exportSurvey(Long id);

    void exportSurvey(Long id, boolean filter);

    void exportAllToVoxco();

    void importVoxcoResponse(boolean recreateExtractions);

    List<NodeVoxcoVO> validateVoxcoQuestions();
}

