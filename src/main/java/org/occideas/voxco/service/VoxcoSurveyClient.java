package org.occideas.voxco.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class VoxcoSurveyClient implements IVoxcoClient<Survey, Long> {
    private static final Logger log = LogManager.getLogger(VoxcoSurveyClient.class);

    private static final String BASE_ENDPOINT = "/api/survey";

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
    public ResponseEntity<Survey> getById(Long id) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/")
                .path(String.valueOf(id));
        try {
            return restTemplate.exchange(builder.build().toUri(), HttpMethod.GET,
                    new HttpEntity<>(getHeaders()), Survey.class);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to get survey for id=" + id + ", cause=" + e.getMessage());
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            throw e;
        }
    }

    @Override
    public ResponseEntity<Void> create(Survey request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/create");
        try {
            return restTemplate.postForEntity(
                    builder.build().toUri(),
                    new HttpEntity<>(request, getHeaders()), Void.class);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to save survey", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<Survey> update(Survey request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/")
                .path(String.valueOf(request.getId()));
        try {
            return restTemplate.postForEntity(builder.build().toUri(),
                    new HttpEntity<>(request, getHeaders()), Survey.class);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to update survey for id=" + request.getId() + ", cause=" + e.getMessage());
            //api seems to return 500 even if was updated successfully
            return getById(request.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        log.warn("Method not supported");
    }

    @Override
    public ResponseEntity<SurveyImportResult> importSurveyAsJson(SurveyImportRequest request, Long surveyId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/import/json/").path(String.valueOf(surveyId));
        try {
            prettyPrint(objectMapper, request);
            return restTemplate.postForEntity(
                    builder.build().toUri(),
                    new HttpEntity<>(request, getHeaders()), SurveyImportResult.class);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to import survey for id=" + surveyId, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadSurveyById(Long surveyId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/export/json/")
                .path(String.valueOf(surveyId))
                .queryParam("deployed", true);
        try {
            return restTemplate.exchange(builder.build().toUri(), HttpMethod.GET,
                    new HttpEntity<>(getHeaders()), byte[].class);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to download survey. surveyId={}. cause=",
                    surveyId, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<Survey>> getUserSurveys() {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public ResponseEntity<SurveyExtractionsResult> getSurveyExtractions(Long surveyId) {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public ResponseEntity<ExtractionResult> getExtractionResult(Long extractionId) {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public ResponseEntity<Void> startSurveyExtraction(Long extractionId) {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public ResponseEntity<byte[]> downloadSurveyExtract(Long extractionId, Long fileId) {
        log.warn("Method not supported");
        return null;
    }
}
