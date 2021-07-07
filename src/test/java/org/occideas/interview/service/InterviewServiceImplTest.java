package org.occideas.interview.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.agent.service.AgentServiceImpl;
import org.occideas.entity.Interview;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.interviewautoassessment.service.InterviewAutoAssessmentServiceImpl;
import org.occideas.interviewfiredrules.service.InterviewFiredRulesServiceImpl;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.mapper.InterviewMapperImpl;
import org.occideas.vo.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewServiceImplTest {

    @Mock
    InterviewDao interviewDao;
    @Mock
    InterviewMapperImpl interviewMapper;
    @Mock
    AgentServiceImpl agentService;
    @Mock
    InterviewQuestionDao interviewQuestionDao;
    @Mock
    InterviewAutoAssessmentServiceImpl autoAssessmentService;
    @Mock
    InterviewFiredRulesServiceImpl firedRulesService;

    @Spy
    @InjectMocks
    InterviewServiceImpl interviewService;

    @Test
    void givenListOfInterviews_whenAutoAssessedRules_shouldReturnSuccess() {
        when(interviewDao.getAllInterviewsWithoutAnswers())
                .thenReturn(buildSampleInterviews());
        List<InterviewVO> processInterviews = buildSampleInterviewVOs();
        when(interviewMapper.convertToInterviewWithoutAnswersList(anyList()))
                .thenReturn(processInterviews);
        when(agentService.getStudyAgents()).thenReturn(buildSampleAgents());
        doReturn(processInterviews.get(0)).when(interviewService).determineFiredRules(any());
        when(autoAssessmentService.findByInterviewId(anyLong()))
            .thenReturn(buildSampleAutoAssessment());
        when(firedRulesService.findByInterviewId(anyLong()))
            .thenReturn(buildInterviewFiredRules());
        doNothing().when(interviewService).update(any());

        List<InterviewVO> interviews = interviewService.autoAssessedRules();

        assertNotNull(interviews);
    }

    private List<InterviewVO> buildSampleInterviewVOs() {
        List<InterviewVO> interviews = new ArrayList<>();
        InterviewVO interview = new InterviewVO();
        interview.setInterviewId(10L);
        interview.setReferenceNumber("FARM_4008");
        interview.setParentId(0L);
        interview.setAssessedStatus("Needs Review");
        interviews.add(interview);
        return interviews;
    }

    List<Interview> buildSampleInterviews(){
        List<Interview> interviews = new ArrayList<>();
        Interview interview = new Interview();
        interview.setIdinterview(10L);
        interview.setReferenceNumber("FARM_4008");
        interview.setParentId(0L);
        interview.setAssessedStatus("Needs Review");
        interviews.add(interview);
        return interviews;
    }

    List<AgentVO> buildSampleAgents(){
        List<AgentVO> agents = new ArrayList<>();
        AgentVO agentVO = buildAgent();
        agents.add(agentVO);
        return agents;
    }

    private AgentVO buildAgent() {
        AgentVO agentVO = new AgentVO();
        agentVO.setIdAgent(72L);
        agentVO.setDeleted(0);
        agentVO.setDiscriminator("A");
        agentVO.setName("1,3 butadiene");
        return agentVO;
    }

    List<InterviewAutoAssessmentVO> buildSampleAutoAssessment(){
        List<InterviewAutoAssessmentVO> interviewAutoAssessments
                = new ArrayList<>();
        InterviewAutoAssessmentVO interviewAutoAssessment
                = new InterviewAutoAssessmentVO();
        interviewAutoAssessment.setIdInterview(10L);
        interviewAutoAssessment.setId(1L);
        interviewAutoAssessment.setRule(buildRule());
        interviewAutoAssessments.add(interviewAutoAssessment);
        return interviewAutoAssessments;
    }

    List<InterviewFiredRulesVO> buildInterviewFiredRules(){
        List<InterviewFiredRulesVO> interviewFiredRules
            = new ArrayList<>();
        InterviewFiredRulesVO interviewFiredRule =
                new InterviewFiredRulesVO();
        interviewFiredRule.setId(1L);
        interviewFiredRule.setIdRule(1L);
        interviewFiredRule.setIdinterview(10L);
        interviewFiredRule.setRules(Collections.singletonList(buildRule()));
        interviewFiredRules.add(interviewFiredRule);
        return interviewFiredRules;
    }

    RuleVO buildRule(){
        RuleVO rule = new RuleVO();
        rule.setIdRule(1L);
        rule.setDeleted(0);
        rule.setLevel("5");
        rule.setLevelValue(5);
        rule.setAgentId(72);
        rule.setAgent(buildAgent());
        return rule;
    }
}