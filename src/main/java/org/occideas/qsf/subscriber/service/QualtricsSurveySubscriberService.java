package org.occideas.qsf.subscriber.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.QualtricsSurveySubscription;
import org.occideas.qsf.IQSFClient;
import org.occideas.qsf.dao.QualtricsSurveySubscriptionDao;
import org.occideas.qsf.response.Element;
import org.occideas.qsf.response.SurveyListResponse;
import org.occideas.qsf.response.SurveyListenResponse;
import org.occideas.qsf.subscriber.constant.QualtricsSubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QualtricsSurveySubscriberService {

    private Logger log = LogManager.getLogger(this);

    @Autowired
    @Lazy
    private IQSFClient iqsfClient;
    @Autowired
    private QualtricsSurveySubscriptionDao qualtricsSurveySubscriptionDao;


    public void subscribeActiveDistributions() {
        log.info("Started distribution subscriber");
        List<String> activeSurveys = getActiveSurveys();
        activeSurveys.forEach(this::listenToSurvey);
        log.info("Successfully subscribed to active distributions");
    }

    public List<String> getActiveSurveys() {
        log.info("Getting list of active surveys");
        Response listSurveyResponse = iqsfClient.listSurvey();
        SurveyListResponse surveyListResponse = (SurveyListResponse) listSurveyResponse.getEntity();
        List<String> activeSurveys = surveyListResponse.getResult()
                .getElements()
                .stream()
                .filter(Element::isActive)
                .map(Element::getId)
                .collect(Collectors.toList());
        log.info("Active Surveys are {}", activeSurveys.size());
        return activeSurveys;
    }

    public void listenToSurvey(String surveyId) {
        Optional<QualtricsSurveySubscription> survey = qualtricsSurveySubscriptionDao.findBySurveyId(surveyId);
        if (survey.isPresent()) {
            log.info("Skip , already listening to survey {}", surveyId);
            return;
        }

        Response listenToSurveyResponse = iqsfClient.listenToSurveyResponse(surveyId);
        SurveyListenResponse surveyListenResponse = (SurveyListenResponse) listenToSurveyResponse.getEntity();
        qualtricsSurveySubscriptionDao.save(createSubscription(surveyId, surveyListenResponse));
        log.info("successfully listening to survey {}", surveyId);
    }

    private QualtricsSurveySubscription createSubscription(String surveyId, SurveyListenResponse surveyListenResponse) {
        QualtricsSurveySubscription qualtricsSurveySubscription = new QualtricsSurveySubscription();
        qualtricsSurveySubscription.setSurveyId(surveyId);
        qualtricsSurveySubscription.setSubscriptionId(surveyListenResponse.getSurveyListenResult().getId());
        qualtricsSurveySubscription.setStatus(QualtricsSubscriptionStatus.ACTIVE);
        qualtricsSurveySubscription.setSubscriptionDate(LocalDateTime.now());
        return qualtricsSurveySubscription;
    }


}
