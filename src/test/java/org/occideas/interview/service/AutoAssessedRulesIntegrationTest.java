package org.occideas.interview.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.Interview;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.interviewfiredrules.dao.InterviewFiredRulesDao;
import org.occideas.vo.InterviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AutoAssessedRulesIntegrationTest {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private InterviewServiceImpl interviewService;
    @Autowired
    private InterviewDao interviewDao;
    @Autowired
    private InterviewFiredRulesDao interviewFiredRulesDao;

    @Test
    public void givenInterviews_whenAutoAssessedRules_shouldReturnInElapsedTime(){
        List<Interview> allInterviewsWithoutAnswers = interviewDao.getAllInterviewsWithoutAnswers();
        Instant start = Instant.now();

        interviewService.autoAssessedRules();

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        Long inserted = interviewFiredRulesDao.count();
        assertTrue(inserted > 0);
        log.info("auto assessed rules took {} ms per interview , total interviews {}", timeElapsed / allInterviewsWithoutAnswers.size(), allInterviewsWithoutAnswers.size());
        // 150ms per interview
        assertTrue(timeElapsed <= (2000*allInterviewsWithoutAnswers.size()));
    }

    @Test
    public void givenInterview_whenUpdateFiredRules_shouldReturnWithFiredRules(){

        InterviewVO interviewVO = interviewService.updateFiredRule(2599L);

        assertFalse(interviewVO.getFiredRules().isEmpty());
    }


}
