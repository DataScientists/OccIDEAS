package org.occideas.qsf.subscriber.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.qsf.IQSFClient;
import org.occideas.qsf.dao.QualtricsSurveyDao;
import org.occideas.qsf.subscriber.constant.QualtricsProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class QualtricsSurveyService {

    private Logger log = LogManager.getLogger(this);

    @Autowired
    private IQSFClient iqsfClient;
    @Autowired
    private QualtricsSurveyDao qualtricsSurveyDao;

    public void consumeSurveyResponse(QualtricsSurvey qualtricsSurvey) {

        if (!QualtricsProcessStatus.COMPLETE.name().equalsIgnoreCase(qualtricsSurvey.getQualtricsStatus())) {
            log.info("survey response status is not completed for {} and is {}",
                    qualtricsSurvey.getResponseId(),
                    qualtricsSurvey.getQualtricsStatus());
            return;
        }

        Optional<QualtricsSurvey> optionalResponse = qualtricsSurveyDao.findById(qualtricsSurvey.getResponseId());
        if (optionalResponse.isPresent()) {
            QualtricsSurvey survey = optionalResponse.get();
            if (Objects.nonNull(survey.getResponse())) {
                log.info("survey response is already there possible duplicate response {}", survey.getResponseId());
                return;
            }
        }

        qualtricsSurvey.setResponse(getResponse(qualtricsSurvey));

        if (Objects.isNull(qualtricsSurvey.getResponse())) {
            log.info("survey response is not available from qualtrics {}", qualtricsSurvey.getResponseId());
            return;
        }

        qualtricsSurveyDao.save(qualtricsSurvey);
    }

    private byte[] getResponse(QualtricsSurvey survey) {
        String response = iqsfClient.getResponse(survey.getResponseId(), survey.getSurveyId());
        if (StringUtils.isEmpty(response)) {
            log.info("survey response is empty from qualtrics {}", survey.getResponseId());
            return null;
        }
        return response.getBytes();
    }


}
