package org.occideas.qsf.subscriber;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.entity.QualtricsSurveySubscription;
import org.occideas.qsf.CommonQSFTestData;
import org.occideas.qsf.IQSFClient;
import org.occideas.qsf.dao.QualtricsSurveySubscriptionDao;
import org.occideas.qsf.response.*;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QualtricsSurveySubscriberServiceTest {

    @Mock
    private IQSFClient iqsfClient;
    @Mock
    private QualtricsSurveySubscriptionDao qualtricsSurveySubscriptionDao;
    @Mock
    private Logger log;
    @InjectMocks
    private QualtricsSurveySubscriberService qualtricsSurveySubscriberService;

    @Test
    void givenActiveSurveysExist_whenGetActiveSurveys_shouldReturnResult() {
        SurveyListResult surveyListResult = new SurveyListResult();
        Element activeElement = CommonQSFTestData.createElement(true, "Active Survey", "1");
        Element inactiveElement = CommonQSFTestData.createElement(false, "Inactive Survey", "2");
        surveyListResult.setElements(Arrays.asList(activeElement, inactiveElement));
        SurveyListResponse surveyListResponse = new SurveyListResponse();
        surveyListResponse.setResult(surveyListResult);
        Response response = Response.ok(surveyListResponse).build();
        when(iqsfClient.listSurvey()).thenReturn(response);

        List<String> activeSurveys = qualtricsSurveySubscriberService.getActiveSurveys();

        assertNotNull(activeSurveys);
        assertEquals(1, activeSurveys.size());
        assertEquals(activeElement.getId(), activeSurveys.get(0));
    }

    @Test
    void givenDistributionExist_whenListenToDistributedSurveys_shouldSubscribeToResponse() {
        DistributionListResponse distributionListResponse = new DistributionListResponse();
        DistributionListResult distributionListResult = new DistributionListResult();
        DistributionListElement distributionListElement = new DistributionListElement();
        distributionListElement.setId("1");
        distributionListResult.setElements(Arrays.asList(distributionListElement));
        distributionListResponse.setResult(distributionListResult);
        when(iqsfClient.listDistribution(anyString())).thenReturn(Response.ok(distributionListResponse).build());
        SurveyListenResponse surveyListenResponse = new SurveyListenResponse();
        SurveyListenResult surveyListenResult = new SurveyListenResult();
        surveyListenResult.setId("1");
        surveyListenResponse.setSurveyListenResult(surveyListenResult);
        when(iqsfClient.listenToSurveyResponse(anyString())).thenReturn(Response.ok(surveyListenResponse).build());
        when(qualtricsSurveySubscriptionDao.findBySurveyId("1")).thenReturn(Optional.ofNullable(null));

        qualtricsSurveySubscriberService.listenToDistributedSurveys("1");

        verify(iqsfClient, times(1)).listenToSurveyResponse(any());
        verify(qualtricsSurveySubscriptionDao, times(1)).save(any(QualtricsSurveySubscription.class));
    }

    @Test
    void givenDistributionExistAndSurveyAlreadyListening_whenListenToDistributedSurveys_shouldNotSubscribeToResponse() {
        when(qualtricsSurveySubscriptionDao.findBySurveyId("1")).thenReturn(Optional.of(new QualtricsSurveySubscription()));

        qualtricsSurveySubscriberService.listenToDistributedSurveys("1");

        verify(iqsfClient, times(0)).listenToSurveyResponse(any());
    }

}