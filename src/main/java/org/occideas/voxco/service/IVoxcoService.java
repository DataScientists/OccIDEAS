package org.occideas.voxco.service;

public interface IVoxcoService {

    void importSurvey(Long id);

    void importSurvey(Long id, boolean filter);

    void importAllToVoxco();

    void importVoxcoResponse(boolean recreateExtractions);
}

