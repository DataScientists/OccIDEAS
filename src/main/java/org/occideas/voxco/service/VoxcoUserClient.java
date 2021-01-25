package org.occideas.voxco.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.voxco.model.Survey;
import org.occideas.voxco.model.User;
import org.occideas.voxco.request.SurveyImportRequest;
import org.occideas.voxco.response.ExtractionResult;
import org.occideas.voxco.response.SurveyExtractionsResult;
import org.occideas.voxco.response.SurveyImportResult;
import org.occideas.voxco.response.UserSurveysResult;
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
public class VoxcoUserClient implements IVoxcoClient<User, Long> {

    private static final Logger log = LogManager.getLogger(VoxcoUserClient.class);

    private static final String BASE_ENDPOINT = "/api/users";

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
    public ResponseEntity<User> getById(Long aLong) {
        log.warn("For implementation later");
        return null;
    }

    @Override
    public ResponseEntity<Void> create(User request) {
        log.warn("For implementation later");
        return null;
    }

    @Override
    public ResponseEntity<User> update(User request) {
        log.warn("For implementation later");
        return null;
    }

    @Override
    public void deleteById(Long id) {
        log.warn("Method not supported");
    }

    @Override
    public ResponseEntity<SurveyImportResult> importSurveyAsJson(SurveyImportRequest request, Long aLong) {
        log.warn("Method not supported");
        return null;
    }

    @Override
    public ResponseEntity<List<Survey>> getUserSurveys() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + BASE_ENDPOINT)
                .path("/user/surveys");
        try {
            ResponseEntity<UserSurveysResult> surveys = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET,
                    new HttpEntity<>(getHeaders()), UserSurveysResult.class);
            return new ResponseEntity<>(surveys.getBody().getSurveys(), HttpStatus.OK);
        } catch (final HttpClientErrorException e) {
            log.error("Unable to get user surveys, cause=" + e.getMessage());
            throw e;
        }
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
