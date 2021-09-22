package org.occideas.qsf.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.InterviewResults;
import org.occideas.interview.dao.InterviewResultDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
@RequestMapping("/view-results")
public class QSFResultsController {

    private final Logger log = LogManager.getLogger(this.getClass());

    private final InterviewResultDao interviewResultDao;

    public QSFResultsController(InterviewResultDao interviewResultDao) {
        this.interviewResultDao = interviewResultDao;
    }

    @GetMapping
    public String getResults(@RequestParam("RID") String responseId) {
        log.info("getResults responseId {}", responseId);
        InterviewResults interviewResults = interviewResultDao.findByReferenceNumber(responseId);
        if (Objects.isNull(interviewResults)) {
        	log.info("No results");
            return null;
        }

        return new String(interviewResults.getResults(), StandardCharsets.UTF_8);
    }
}
