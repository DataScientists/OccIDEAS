package org.occideas.qsf.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.QualtricsSurvey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class QualtricsSurveyDaoTest {

    @Autowired
    QualtricsSurveyDao qualtricsSurveyDao;

    @AfterEach
    public void cleanup() {
        qualtricsSurveyDao.deleteAll();
    }

    @Test
    void givenResponseIdExist_whenFindByResponseId_shouldReturnRecord() {
        QualtricsSurvey qualtricsSurvey
                = new QualtricsSurvey();
        qualtricsSurvey.setResponseId("1");
        qualtricsSurvey.setSurveyId("1");
        qualtricsSurvey.setBrandId("1B");
        qualtricsSurvey.setTopic("topic");
        qualtricsSurvey.setCompletedDate(LocalDateTime.now());
        qualtricsSurvey.setQualtricsStatus("Completed");
        qualtricsSurvey.setResponse("{test: test}".getBytes());
        qualtricsSurveyDao.save(qualtricsSurvey);

        List<QualtricsSurvey> listOfSurvey = qualtricsSurveyDao.findByResponseId("1");

        assertFalse(listOfSurvey.isEmpty());
    }

    @Test
    void givenResponseIdNotExist_whenFindById_shouldNotReturnRecord() {
        List<QualtricsSurvey> listOfSurvey = qualtricsSurveyDao.findByResponseId("1");

        assertTrue(listOfSurvey.isEmpty());
    }

    @Test
    void givenIsProcessed_whenFindByIsProcessed_shouldReturnSurvey() {
        QualtricsSurvey qualtricsSurvey
                = new QualtricsSurvey();
        qualtricsSurvey.setResponseId("1");
        qualtricsSurvey.setSurveyId("1");
        qualtricsSurvey.setBrandId("1B");
        qualtricsSurvey.setTopic("topic");
        qualtricsSurvey.setCompletedDate(LocalDateTime.now());
        qualtricsSurvey.setQualtricsStatus("Completed");
        qualtricsSurvey.setResponse("{test: test}".getBytes());
        qualtricsSurvey.setProcessed(false);
        qualtricsSurveyDao.save(qualtricsSurvey);

        List<QualtricsSurvey> list = qualtricsSurveyDao.findByIsProcessed(false);

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void givenIsProcessedTrue_whenFindByIsProcessed_shouldNotReturnSurvey() {
        QualtricsSurvey qualtricsSurvey
                = new QualtricsSurvey();
        qualtricsSurvey.setResponseId("1");
        qualtricsSurvey.setSurveyId("1");
        qualtricsSurvey.setBrandId("1B");
        qualtricsSurvey.setTopic("topic");
        qualtricsSurvey.setCompletedDate(LocalDateTime.now());
        qualtricsSurvey.setQualtricsStatus("Completed");
        qualtricsSurvey.setResponse("{test: test}".getBytes());
        qualtricsSurvey.setProcessed(true);
        qualtricsSurveyDao.save(qualtricsSurvey);

        List<QualtricsSurvey> list = qualtricsSurveyDao.findByIsProcessed(false);

        assertTrue(list.isEmpty());
    }

    @Test
    void givenErrorNotEmpty_whenFindByIsProcessed_shouldNotReturnSurvey() {
        QualtricsSurvey qualtricsSurvey
                = new QualtricsSurvey();
        qualtricsSurvey.setResponseId("1");
        qualtricsSurvey.setSurveyId("1");
        qualtricsSurvey.setBrandId("1B");
        qualtricsSurvey.setTopic("topic");
        qualtricsSurvey.setCompletedDate(LocalDateTime.now());
        qualtricsSurvey.setQualtricsStatus("Completed");
        qualtricsSurvey.setResponse("{test: test}".getBytes());
        qualtricsSurvey.setProcessed(false);
        qualtricsSurvey.setError("Error");
        qualtricsSurveyDao.save(qualtricsSurvey);

        List<QualtricsSurvey> list = qualtricsSurveyDao.findByIsProcessed(false);

        assertTrue(list.isEmpty());
    }

    @Test
    void givenIsProcessedAndResponseIsNull_whenFindByIsProcessed_shouldReturnSurvey() {
        QualtricsSurvey qualtricsSurvey
                = new QualtricsSurvey();
        qualtricsSurvey.setResponseId("1");
        qualtricsSurvey.setSurveyId("1");
        qualtricsSurvey.setBrandId("1B");
        qualtricsSurvey.setTopic("topic");
        qualtricsSurvey.setCompletedDate(LocalDateTime.now());
        qualtricsSurvey.setQualtricsStatus("Completed");
        qualtricsSurvey.setResponse(null);
        qualtricsSurvey.setProcessed(false);
        qualtricsSurveyDao.save(qualtricsSurvey);

        List<QualtricsSurvey> list = qualtricsSurveyDao.findByIsProcessed(false);

        assertTrue(list.isEmpty());
    }

    @Test
    void givenResponseIdExist_whenDeleteByResponseId_shouldSuccess() {
        QualtricsSurvey qualtricsSurvey
                = new QualtricsSurvey();
        qualtricsSurvey.setResponseId("12345");
        qualtricsSurvey.setSurveyId("1");
        qualtricsSurvey.setBrandId("1B");
        qualtricsSurvey.setTopic("topic");
        qualtricsSurvey.setCompletedDate(LocalDateTime.now());
        qualtricsSurvey.setQualtricsStatus("Completed");
        qualtricsSurvey.setResponse("{test: test}".getBytes());
        qualtricsSurveyDao.save(qualtricsSurvey);
        List<QualtricsSurvey> listOfSurvey = qualtricsSurveyDao.findByResponseId("12345");
        assertFalse(listOfSurvey.isEmpty());

        qualtricsSurveyDao.deleteByResponseId("12345");

        List<QualtricsSurvey> actual = qualtricsSurveyDao.findByResponseId("12345");
        assertTrue(actual.isEmpty());
    }

    @Test
    void givenResponseIdNotExist_whenDeleteByResponseId_shouldSuccess() {

        qualtricsSurveyDao.deleteByResponseId("12345");

        List<QualtricsSurvey> actual = qualtricsSurveyDao.findByResponseId("12345");
        assertTrue(actual.isEmpty());
    }
}