package org.occideas.interviewfiredrules.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.occideas.entity.InterviewFiredRules;
import org.occideas.rule.dao.RuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InterviewFiredRulesDaoTest {

    @Autowired
    InterviewFiredRulesDao interviewFiredRulesDao;
    @Autowired
    RuleDao ruleDao;

    @Test
    void givenInterviewFiredRule_whenSave_shouldSaveSuccessfully() {
        InterviewFiredRules interviewFiredRules = buildInterviewFiredRules(1l, 1l);

        InterviewFiredRules actual = interviewFiredRulesDao.save(interviewFiredRules);

        assertTrue(actual.getId() > 0);
    }

    @Test
    void givenInterviewFiredRules_whenSaveAll_shouldSaveSuccessfully() {
        List<InterviewFiredRules> interviewFiredRules = new ArrayList<>();
        InterviewFiredRules interviewFiredRules1 = buildInterviewFiredRules(1l, 1l);
        InterviewFiredRules interviewFiredRules2 = buildInterviewFiredRules(2l, 2l);
        interviewFiredRules.add(interviewFiredRules1);
        interviewFiredRules.add(interviewFiredRules2);

        interviewFiredRulesDao.saveAll(interviewFiredRules);

        assertTrue(interviewFiredRules1.getId() > 0);
        assertTrue(interviewFiredRules2.getId() > 0);
    }

    @Test
    void givenExistingInterviews_whenFindByInterviewId_shouldReturnIncludingRules() {
        InterviewFiredRules interviewFiredRules = buildInterviewFiredRules(2600, 6000);
        interviewFiredRulesDao.save(interviewFiredRules);

        List<InterviewFiredRules> actual = interviewFiredRulesDao.findByInterviewIdWithRules(2600l);

        assertFalse(actual.isEmpty());
        assertNotNull(actual.get(0).getRules());
        assertFalse(actual.get(0).getRules().isEmpty());
    }

    private InterviewFiredRules buildInterviewFiredRules(long idInterview, long idRule){
        InterviewFiredRules interviewFiredRules =
                new InterviewFiredRules();
        interviewFiredRules.setIdinterview(idInterview);
        interviewFiredRules.setIdRule(idRule);
        return interviewFiredRules;
    }

}