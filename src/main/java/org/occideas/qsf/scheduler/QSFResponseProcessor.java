package org.occideas.qsf.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.interview.service.InterviewService;
import org.occideas.qsf.dao.QualtricsSurveyDao;
import org.occideas.qsf.response.SurveyResponse;
import org.occideas.qsf.service.IQSFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class QSFResponseProcessor {

    private Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private QualtricsSurveyDao qualtricsSurveyDao;
    @Autowired
    private IQSFService iqsfService;
    @Autowired
    private InterviewService interviewService;

    @Scheduled(fixedRate = 10000)
    public void processQSFResponse() {
        List<QualtricsSurvey> unprocessedSurveys = qualtricsSurveyDao.findByIsProcessed(false);
        if (!unprocessedSurveys.isEmpty()) {
            log.info("Started to check for unprocessed surveys " + unprocessedSurveys.size());
            unprocessedSurveys.forEach(unprocessedSurvey -> {
                SurveyResponse surveyResponse = null;
                try {
                    surveyResponse = translateQSFResponse(unprocessedSurvey.getResponse());
                    interviewService.updateQualtricsResults(iqsfService.processResponse(unprocessedSurvey.getSurveyId(),unprocessedSurvey.getTopic(), surveyResponse.getResult()));
                    unprocessedSurvey.setProcessed(true);
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                    unprocessedSurvey.setError(e.getMessage());
                }

                qualtricsSurveyDao.save(unprocessedSurvey);
            });
            log.info("End processing of unprocessed surveys count {}", unprocessedSurveys.size());
        }
    }

    public SurveyResponse translateQSFResponse(byte[] response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, new TypeReference<SurveyResponse>() {
        });
    }

}
