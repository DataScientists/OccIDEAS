package org.occideas.qsf.scheduler;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.qsf.dao.QualtricsSurveyDao;
import org.occideas.qsf.service.IQSFService;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QSFResponseProcessorTest {

    @Mock
    QualtricsSurveyDao qualtricsSurveyDao;
    @Mock
    IQSFService iqsfService;

    @Mock
    private Logger log;

    @InjectMocks
    QSFResponseProcessor qsfResponseProcessor;

    @Test
    void givenUnprocessedSurveys_whenProcessResponse_shouldProcessUnprocessedSurveys() {
        QualtricsSurvey qualtricsSurvey = new QualtricsSurvey();
        qualtricsSurvey.setProcessed(false);
        qualtricsSurvey.setSurveyId("1");
        qualtricsSurvey.setResponseId("1a");
        qualtricsSurvey.setResponse("{test: test}".getBytes());
        when(qualtricsSurveyDao.findByIsProcessed(false)).thenReturn(Arrays.asList(qualtricsSurvey));

        qsfResponseProcessor.processQSFResponse();

        verify(qualtricsSurveyDao, times(1)).findByIsProcessed(false);
    }

    @Test
    void givenJsonResponse_whenTranslateQSFResponse_shouldReturnResponseObject() {
    }
}