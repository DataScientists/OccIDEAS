package org.occideas.qsf.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.QualtricsSurveyResponse;
import org.occideas.qsf.dao.QualtricsSurveyResponseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/view-results")
public class QSFResultsController {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private QualtricsSurveyResponseDao qualtricsSurveyResponseDao;

    @GetMapping
    public String getResults(@RequestParam("SID") String surveyId, @RequestParam("RID") String responseId) {
        log.info("survey id {} , responseId {}", surveyId, responseId);
        QualtricsSurveyResponse bySurveyAndResponseId = qualtricsSurveyResponseDao.findBySurveyAndResponseId(surveyId, responseId);
        String response = new String(bySurveyAndResponseId.getResults(), StandardCharsets.UTF_8);
        return response;
    }
}
