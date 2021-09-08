package org.occideas.qsf.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.QualtricsSurvey;
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

    @Scheduled(fixedRate = 5000)
    public void processQSFResponse() {
        log.info("Started to check for unprocessed surveys");
        List<QualtricsSurvey> unprocessedSurveys = qualtricsSurveyDao.findByIsProcessed(false);
        if (!unprocessedSurveys.isEmpty()) {
            unprocessedSurveys.forEach(unprocessedSurvey -> {
                SurveyResponse surveyResponse = null;
                try {
                    surveyResponse = translateQSFResponse(unprocessedSurvey.getResponse());
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                iqsfService.processResponse(unprocessedSurvey.getSurveyId(), surveyResponse.getResult());
                unprocessedSurvey.setProcessed(true);
                qualtricsSurveyDao.save(unprocessedSurvey);
            });
        }
        log.info("End processing of unprocessed surveys count {}", unprocessedSurveys.size());
    }

    public SurveyResponse translateQSFResponse(byte[] response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, new TypeReference<SurveyResponse>() {
        });
    }

}
