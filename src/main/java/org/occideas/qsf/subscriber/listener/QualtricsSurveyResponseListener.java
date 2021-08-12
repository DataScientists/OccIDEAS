package org.occideas.qsf.subscriber.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.qsf.subscriber.service.QualtricsSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("${public.api.base-path}/qualtrics")
public class QualtricsSurveyResponseListener {

    private Logger log = LogManager.getLogger(this);

    @Autowired
    private QualtricsSurveyService qualtricsSurveyService;

    @PostMapping("/response/consumer")
    public ResponseEntity consume(@RequestParam("CompletedDate") String completedDate,
                                  @RequestParam("Status") String status,
                                  @RequestParam("ResponseID") String responseId,
                                  @RequestParam("BrandID") String brandId,
                                  @RequestParam("Topic") String topic,
                                  @RequestParam("SurveyID") String surveyId
    ) {
        try {
            logMessageReceived(completedDate, status, responseId, brandId, topic, surveyId);
            QualtricsSurvey qualtricsSurvey = new QualtricsSurvey();
            qualtricsSurvey.setResponseId(responseId);
            qualtricsSurvey.setSurveyId(surveyId);
            qualtricsSurvey.setBrandId(brandId);
            qualtricsSurvey.setTopic(topic);
            qualtricsSurvey.setCompletedDate(LocalDateTime.now());
            qualtricsSurvey.setQualtricsStatus(status);
            qualtricsSurveyService.consumeSurveyResponse(qualtricsSurvey);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    private void logMessageReceived(String completedDate, String status, String responseId, String brandId, String topic, String surveyId) {
        log.info(" Received survey response: " +
                "  Survey ID <{}> " +
                "  Brand ID <{}> " +
                "  Response ID <{}> " +
                "  Status <{}> " +
                "  Completed Date <{}> " +
                "  Topic <{}>.",
                surveyId,
                brandId,
                responseId,
                status,
                completedDate,
                topic);
    }

}
