package org.occideas.qsf.scheduler;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.qsf.dao.QualtricsSurveyDao;
import org.occideas.qsf.response.SurveyResponse;
import org.occideas.qsf.service.IQSFService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        qualtricsSurvey.setResponse(readFile());
        when(qualtricsSurveyDao.findByIsProcessed(false)).thenReturn(Arrays.asList(qualtricsSurvey));
        when(qualtricsSurveyDao.save(qualtricsSurvey)).thenReturn(qualtricsSurvey);

        qsfResponseProcessor.processQSFResponse();

        verify(qualtricsSurveyDao, times(1)).findByIsProcessed(false);
        verify(qualtricsSurveyDao, times(1)).save(qualtricsSurvey);
    }

    @Test
    void givenJsonResponse_whenTranslateQSFResponse_shouldReturnResponseObject() throws IOException {
        byte[] sampleResponse = readFile();

        SurveyResponse surveyResponse = qsfResponseProcessor.translateQSFResponse(sampleResponse);

        assertNotNull(surveyResponse);
        assertNotNull(surveyResponse.getResult());
        assertNotNull(surveyResponse.getResult().getResponseId());
    }

    private byte[] readFile() {
        String path = "mock/sample_response.json";
        try {
            String content = Files.readString(Paths.get(ClassLoader.getSystemResource(path).toURI()));
            return content.getBytes(StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}