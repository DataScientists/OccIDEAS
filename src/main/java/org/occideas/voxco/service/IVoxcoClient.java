package org.occideas.voxco.service;

import org.occideas.voxco.request.SurveyImportRequest;
import org.occideas.voxco.response.SurveyImportResult;
import org.springframework.http.ResponseEntity;

public interface IVoxcoClient<T, ID> {

    ResponseEntity<T> getById(ID id);

    ResponseEntity<Void> create(T request);

    ResponseEntity<T> update(T request);

    ResponseEntity<SurveyImportResult> importSurveyAsJson(SurveyImportRequest request, ID id);
}
