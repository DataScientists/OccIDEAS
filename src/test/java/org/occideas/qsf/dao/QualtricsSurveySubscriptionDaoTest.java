package org.occideas.qsf.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.QualtricsSurveySubscription;
import org.occideas.qsf.subscriber.QualtricsSubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class QualtricsSurveySubscriptionDaoTest {

    @Autowired
    QualtricsSurveySubscriptionDao qualtricsSurveySubscriptionDao;

    @AfterEach
    public void cleanup() {
        qualtricsSurveySubscriptionDao.deleteAll();
    }

    @Test
    void givenSurveyIdExist_whenFindBySurveyId_shouldReturnRecord() {
        QualtricsSurveySubscription qualtricsSurveySubscription
                = new QualtricsSurveySubscription();
        qualtricsSurveySubscription.setSurveyId("1");
        qualtricsSurveySubscription.setSubscriptionId("1");
        qualtricsSurveySubscription.setSubscriptionDate(LocalDateTime.now());
        qualtricsSurveySubscription.setStatus(QualtricsSubscriptionStatus.ACTIVE);
        qualtricsSurveySubscriptionDao.save(qualtricsSurveySubscription);

        Optional<QualtricsSurveySubscription> optionalSurveySubscription = qualtricsSurveySubscriptionDao.findBySurveyId("1");

        assertTrue(optionalSurveySubscription.isPresent());
    }

    @Test
    void givenSurveyIdNotExist_whenFindBySurveyId_shouldNotReturnRecord() {
        Optional<QualtricsSurveySubscription> optionalSurveySubscription = qualtricsSurveySubscriptionDao.findBySurveyId("1");

        assertTrue(optionalSurveySubscription.isEmpty());
    }
}