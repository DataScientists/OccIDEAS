package org.occideas.qsf.subscriber.service;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.qsf.IQSFClient;
import org.occideas.qsf.dao.QualtricsSurveyDao;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QualtricsSurveyServiceTest {

    @Mock
    private IQSFClient iqsfClient;
    @Mock
    private QualtricsSurveyDao qualtricsSurveyDao;
    @Mock
    private Logger log;

    @InjectMocks
    private QualtricsSurveyService qualtricsSurveyService;

    @Test
    public void givenSurveyResponseCompleted_whenConsumeSurveyResponse_shouldSaveResponse() {
        QualtricsSurvey qualtricsSurvey = new QualtricsSurvey();
        qualtricsSurvey.setResponseId("1r");
        qualtricsSurvey.setSurveyId("1s");
        qualtricsSurvey.setBrandId("1");
        qualtricsSurvey.setTopic("topic");
        qualtricsSurvey.setCompletedDate(LocalDateTime.now());
        qualtricsSurvey.setQualtricsStatus("Complete");
        when(iqsfClient.getResponse(anyString(), anyString())).thenReturn("{test: test}");

        qualtricsSurveyService.consumeSurveyResponse(qualtricsSurvey);

        verify(qualtricsSurveyDao, times(1)).save(qualtricsSurvey);
    }

    @Test
    public void givenSurveyResponseNotCompleted_whenConsumeSurveyResponse_shouldNotSaveResponse() {
        QualtricsSurvey qualtricsSurvey = new QualtricsSurvey();
        qualtricsSurvey.setResponseId("1");
        qualtricsSurvey.setSurveyId("1");
        qualtricsSurvey.setBrandId("1");
        qualtricsSurvey.setTopic("topic");
        qualtricsSurvey.setCompletedDate(LocalDateTime.now());
        qualtricsSurvey.setQualtricsStatus("Not Completed");

        qualtricsSurveyService.consumeSurveyResponse(qualtricsSurvey);

        verify(qualtricsSurveyDao, times(0)).findByResponseId(anyString());
        verify(qualtricsSurveyDao, times(0)).save(qualtricsSurvey);
    }

}