package org.occideas.voxco.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.voxco.model.Survey;
import org.occideas.voxco.request.SurveyImportRequest;
import org.occideas.voxco.response.ExtractionResult;
import org.occideas.voxco.response.SurveyExtractionsResult;
import org.occideas.voxco.response.SurveyImportResult;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IVoxcoClient<T, ID> {

    Logger log = LogManager.getLogger(IVoxcoClient.class);

    ResponseEntity<T> getById(ID id);

    ResponseEntity<Void> create(T request);

    ResponseEntity<T> update(T request);

    void deleteById(ID id);

    ResponseEntity<SurveyImportResult> importSurveyAsJson(SurveyImportRequest request, ID id);

    ResponseEntity<byte[]> downloadSurveyById(ID id);

    ResponseEntity<List<Survey>> getUserSurveys();

    ResponseEntity<SurveyExtractionsResult> getSurveyExtractions(Long surveyId);

    ResponseEntity<ExtractionResult> getExtractionResult(Long extractionId);

    ResponseEntity<Void> startSurveyExtraction(Long extractionId);

    ResponseEntity<byte[]> downloadSurveyExtract(Long extractionId, Long fileId);

    default void prettyPrint(final ObjectMapper objectMapper, Object payload) {
        try {
            log.debug("payload: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            //ignore
        }
    }
}
