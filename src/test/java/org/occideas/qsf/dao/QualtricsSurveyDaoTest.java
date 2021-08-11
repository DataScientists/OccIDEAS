package org.occideas.qsf.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.QualtricsSurvey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

        Optional<QualtricsSurvey> optionalSurvey = qualtricsSurveyDao.findById("1");

        assertTrue(optionalSurvey.isPresent());
    }

    @Test
    void givenResponseIdNotExist_whenFindById_shouldNotReturnRecord() {
        Optional<QualtricsSurvey> optionalSurvey = qualtricsSurveyDao.findById("1");

        assertTrue(optionalSurvey.isEmpty());
    }

}