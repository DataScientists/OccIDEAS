package org.occideas.voxco.service;

public interface IVoxcoService {

    void exportSurvey(Long id);

    void exportSurvey(Long id, boolean filter);

    void exportAllToVoxco();

    void importVoxcoResponse(boolean recreateExtractions);
}

