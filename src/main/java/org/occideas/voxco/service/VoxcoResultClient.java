package org.occideas.voxco.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.voxco.model.Extraction;
import org.occideas.voxco.model.Survey;
import org.occideas.voxco.request.SurveyImportRequest;
import org.occideas.voxco.response.ExtractionResult;
import org.occideas.voxco.response.SurveyExtractionsResult;
import org.occideas.voxco.response.SurveyImportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class VoxcoResultClient implements IVoxcoClient<Extraction, Long> {
    private static final Logger log = LogManager.getLogger(VoxcoResultClient.class);

    private static final String BASE_ENDPOINT = "/api/results/extract";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${voxco.api.key}")
    private String apiKey;

    @Value("${voxco.base.url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client " + apiKey);
        return headers;
    }

    @Override
    public ResponseEntity<Extraction> getById(Long aLong) {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public ResponseEntity<Void> create(Extraction request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT);
        try {
            prettyPrint(objectMapper, request);
            return restTemplate.postForEntity(
                    builder.build().toUri(),
                    new HttpEntity<>(request, getHeaders()), Void.class);
        } catch (final HttpClientErrorException e) {
            if (HttpStatus.BAD_REQUEST.equals(e.getStatusCode())) {
                log.warn("extraction might already exist. response={}", e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            log.error("Unable to save extraction. cause={}", e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<Extraction> update(Extraction request) {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public void deleteById(Long id) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/")
                .path(String.valueOf(id));
        try {
            log.debug("deleting extraction id={}, endpoint={}", id, builder.build().toString());
            restTemplate.exchange(builder.build().toUri(), HttpMethod.DELETE,
                    new HttpEntity<>(getHeaders()), Void.class);
        } catch (final HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                log.warn("extraction id={} not found", id);
                return;
            }
            log.error("Unable to delete extraction id={}. cause={}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<SurveyImportResult> importSurveyAsJson(SurveyImportRequest request, Long aLong) {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public ResponseEntity<byte[]> downloadSurveyById(Long aLong) {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public ResponseEntity<List<Survey>> getUserSurveys() {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public ResponseEntity<SurveyExtractionsResult> getSurveyExtractions(Long surveyId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .queryParam("surveyId", surveyId);
        try {
            return restTemplate.exchange(builder.build().toUri(), HttpMethod.GET,
                    new HttpEntity<>(getHeaders()), SurveyExtractionsResult.class);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to get extractions for survey id=" + surveyId + ", cause=" + e.getMessage());
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            throw e;
        }
    }

    @Override
    public ResponseEntity<ExtractionResult> getExtractionResult(Long extractionId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/")
                .path(String.valueOf(extractionId));
        try {
            return restTemplate.exchange(builder.build().toUri(), HttpMethod.GET,
                    new HttpEntity<>(getHeaders()), ExtractionResult.class);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to get extraction for id=" + extractionId + ", cause=" + e.getMessage());
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            throw e;
        }
    }

    @Override
    public ResponseEntity<Void> startSurveyExtraction(Long extractionId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/")
                .path(String.valueOf(extractionId))
                .path("/start");
        try {
            return restTemplate.postForEntity(builder.build().toUri(),
                    new HttpEntity<>(getHeaders()), Void.class);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to start extraction", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadSurveyExtract(Long extractionId, Long fileId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/file")
                .queryParam("extractionId", extractionId)
                .queryParam("fileId", fileId);

        try {
            return restTemplate.exchange(builder.build().toUri(), HttpMethod.GET,
                    new HttpEntity<>(getHeaders()), byte[].class);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to download extract. extractionId={}, fileId={}. cause=",
                    extractionId, fileId, e.getMessage());
            throw e;
        }
    }
}
