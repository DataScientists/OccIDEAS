package org.occideas.voxco.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.voxco.model.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class VoxcoSurveyService {
    private static final Logger log = LogManager.getLogger(VoxcoSurveyService.class);

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

    public ResponseEntity create(Survey survey) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl).path("/api/survey/create");
        try {
            return restTemplate.postForEntity(
                    builder.build().toUri(),
                    new HttpEntity<>(survey, getHeaders()), String.class);
        } catch (final HttpClientErrorException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
