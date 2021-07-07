package org.occideas.interview.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.agent.service.AgentServiceImpl;
import org.occideas.entity.Interview;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.mapper.InterviewMapperImpl;
import org.occideas.vo.AgentVO;
import org.occideas.vo.InterviewVO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterviewServiceImplTest {

    @Mock
    InterviewDao interviewDao;
    @Mock
    InterviewMapperImpl interviewMapper;
    @Mock
    AgentServiceImpl agentService;
    @InjectMocks
    InterviewServiceImpl interviewService;

    @Test
    void givenListOfInterviews_whenAutoAssessedRules_shouldReturnSuccess() {
        when(interviewDao.getAllInterviewsWithoutAnswers())
                .thenReturn(buildSampleInterviews());
        when(interviewMapper.convertToInterviewWithoutAnswersList(anyList()))
                .thenCallRealMethod();
        when(agentService.getStudyAgents()).thenReturn(buildSampleAgents());

        List<InterviewVO> interviews = interviewService.autoAssessedRules();

        assertNotNull(interviews);
    }

    List<Interview> buildSampleInterviews(){
        List<Interview> interviews = new ArrayList<>();
        return interviews;
    }

    List<AgentVO> buildSampleAgents(){
        List<AgentVO> agents = new ArrayList<>();
        return agents;
    }
}