package org.occideas.qsf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.occideas.config.QualtricsConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/survey-link")
public class QSFIntroRestController {

    private final QualtricsConfig qualtricsConfig;

    public QSFIntroRestController(QualtricsConfig qualtricsConfig) {
        this.qualtricsConfig = qualtricsConfig;
    }

    @GetMapping
    public ResponseEntity<String> getSurveyLink() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(qualtricsConfig.getSurvey()));
    }

}
