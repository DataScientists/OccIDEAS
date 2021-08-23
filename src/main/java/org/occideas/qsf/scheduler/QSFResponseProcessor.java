package org.occideas.qsf.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.qsf.dao.QualtricsSurveyDao;
import org.occideas.qsf.response.Response;
import org.occideas.qsf.service.IQSFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QSFResponseProcessor {

    private Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private QualtricsSurveyDao qualtricsSurveyDao;
    @Autowired
    private IQSFService iqsfService;

    //    @Scheduled(fixedRate = 5000)
    public void processQSFResponse() {
        log.info("Started to check for unprocessed surveys");
        List<QualtricsSurvey> unprocessedSurveys = qualtricsSurveyDao.findByIsProcessed(false);
        if (!unprocessedSurveys.isEmpty()) {
            unprocessedSurveys.forEach(unprocessedSurvey -> {
                Response response = translateQSFResponse(unprocessedSurvey.getResponse());
                iqsfService.processResponse(response);
            });
        }
        log.info("End processing of unprocessed surveys count {}", unprocessedSurveys.size());
    }

    public Response translateQSFResponse(byte[] response) {
        return null;
    }

}
