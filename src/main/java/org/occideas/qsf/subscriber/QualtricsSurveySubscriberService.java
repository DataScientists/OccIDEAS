package org.occideas.qsf.subscriber;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.QualtricsSurveySubscription;
import org.occideas.qsf.IQSFClient;
import org.occideas.qsf.dao.QualtricsSurveySubscriptionDao;
import org.occideas.qsf.response.*;
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
        activeSurveys.forEach(this::listenToDistributedSurveys);
        log.info("Successfully subscribed to active distributions");
    }

    protected List<String> getActiveSurveys() {
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

    protected void listenToDistributedSurveys(String surveyId) {
        Optional<QualtricsSurveySubscription> survey = qualtricsSurveySubscriptionDao.findBySurveyId(surveyId);
        if (survey.isPresent()) {
            log.info("Skip , already listening to survey {}", surveyId);
            return;
        }
        log.info("Getting distribution lists for survey {}", surveyId);
        Response listDistributionResponse = iqsfClient.listDistribution(surveyId);
        DistributionListResponse distributionListResponse = (DistributionListResponse) listDistributionResponse.getEntity();
        log.info("Distribution list for survey {} , {}", surveyId, distributionListResponse.toString());
        List<DistributionListElement> distributionListElements = distributionListResponse.getResult().getElements();
        if (!distributionListElements.isEmpty()) {
            log.info("Start waiting for response {}", distributionListElements.size());
            Response listenToSurveyResponse = iqsfClient.listenToSurveyResponse(surveyId);
            SurveyListenResponse surveyListenResponse = (SurveyListenResponse) listenToSurveyResponse.getEntity();
            qualtricsSurveySubscriptionDao.save(createSubscription(surveyId, surveyListenResponse));
        }
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
