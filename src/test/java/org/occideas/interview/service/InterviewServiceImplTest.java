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
import org.occideas.interviewanswer.service.InterviewAnswerServiceImpl;
import org.occideas.interviewautoassessment.service.InterviewAutoAssessmentServiceImpl;
import org.occideas.interviewfiredrules.service.InterviewFiredRulesServiceImpl;
import org.occideas.interviewmanualassessment.service.InterviewManualAssessmentServiceImpl;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.mapper.InterviewMapperImpl;
import org.occideas.modulerule.service.ModuleRuleService;
import org.occideas.utilities.AssessmentStatusEnum;
import org.occideas.vo.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.occideas.utilities.AssessmentStatusEnum.AUTOASSESSED;
import static org.occideas.utilities.AssessmentStatusEnum.NEEDSREVIEW;

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
    @Mock
    InterviewAnswerServiceImpl interviewAnswerService;
    @Mock
    ModuleRuleService moduleRuleService;
    @Mock
    InterviewManualAssessmentServiceImpl manualAssessmentService;

    @Spy
    @InjectMocks
    InterviewServiceImpl interviewService;

    @Test
    void givenListOfInterviews_whenAutoAssessedRules_shouldReturnNoExposure() {
        final long ID = 1L;
        when(interviewDao.getAllInterviewsWithoutAnswers())
                .thenReturn(buildSampleInterviews(1L));
        List<InterviewVO> processInterviews = buildSampleInterviewVOs(1L, NEEDSREVIEW);
        when(interviewMapper.convertToInterviewWithoutAnswersList(anyList()))
                .thenReturn(processInterviews);
        when(agentService.getStudyAgents()).thenReturn(buildSampleAgents());
        doReturn(processInterviews.get(0)).when(interviewService).determineFiredRules(any());
        when(autoAssessmentService.findByInterviewId(anyLong()))
            .thenReturn(buildSampleAutoAssessment(5));
        when(firedRulesService.findByInterviewId(anyLong()))
            .thenReturn(buildInterviewFiredRules(5));
        doNothing().when(interviewService).update(any());

        List<InterviewVO> interviews = interviewService.autoAssessedRules();

        assertNotNull(interviews);
        Optional<InterviewVO> needsReviewInterview = interviews.stream().filter(interviewVO -> interviewVO.getInterviewId() == ID).findFirst();
        assertTrue(needsReviewInterview.isPresent());
        assertEquals(NEEDSREVIEW.getDisplay(), needsReviewInterview.get().getAssessedStatus());
        List<RuleVO> autoAssessedRulesFromNeedsReviewInterview = needsReviewInterview.get().getAutoAssessedRules();
        assertNotNull(autoAssessedRulesFromNeedsReviewInterview);
        assertFalse(autoAssessedRulesFromNeedsReviewInterview.isEmpty());
        RuleVO deletedRuleFromNeedsReviewInterview = autoAssessedRulesFromNeedsReviewInterview.get(0);
        assertEquals(1L, deletedRuleFromNeedsReviewInterview.getIdRule());
        assertEquals(5, deletedRuleFromNeedsReviewInterview.getLevelValue());
        assertEquals(Integer.valueOf(1), deletedRuleFromNeedsReviewInterview.getDeleted());
        assertNotNull(deletedRuleFromNeedsReviewInterview.getAgent());
        RuleVO activeRuleFromNeedsReviewInterview = autoAssessedRulesFromNeedsReviewInterview.get(1);
        assertEquals(0L, activeRuleFromNeedsReviewInterview.getIdRule());
        assertEquals(5, activeRuleFromNeedsReviewInterview.getLevelValue());
        assertEquals(Integer.valueOf(0),activeRuleFromNeedsReviewInterview.getDeleted());
        assertNull(activeRuleFromNeedsReviewInterview.getAgent());
    }

    @Test
    void givenListOfInterviews_whenAutoAssessedRules_shouldReturnExposure() {
        final long ID = 1L;
        when(interviewDao.getAllInterviewsWithoutAnswers())
                .thenReturn(buildSampleInterviews(1L));
        List<InterviewVO> processInterviews = buildSampleInterviewVOs(1L, AssessmentStatusEnum.NOTASSESSED);
        when(interviewMapper.convertToInterviewWithoutAnswersList(anyList()))
                .thenReturn(processInterviews);
        when(agentService.getStudyAgents()).thenReturn(buildSampleAgents());
        doReturn(processInterviews.get(0)).when(interviewService).determineFiredRules(any());
        when(autoAssessmentService.findByInterviewId(anyLong()))
                .thenReturn(buildSampleAutoAssessment(3));
        when(firedRulesService.findByInterviewId(anyLong()))
                .thenReturn(buildInterviewFiredRules(3));
        doNothing().when(interviewService).update(any());

        List<InterviewVO> interviews = interviewService.autoAssessedRules();

        assertNotNull(interviews);
        Optional<InterviewVO> needsReviewInterview = interviews.stream().filter(interviewVO -> interviewVO.getInterviewId() == ID).findFirst();
        assertTrue(needsReviewInterview.isPresent());
        assertEquals(AUTOASSESSED.getDisplay(), needsReviewInterview.get().getAssessedStatus());
        List<RuleVO> autoAssessedRulesFromNeedsReviewInterview = needsReviewInterview.get().getAutoAssessedRules();
        assertNotNull(autoAssessedRulesFromNeedsReviewInterview);
        assertFalse(autoAssessedRulesFromNeedsReviewInterview.isEmpty());
        RuleVO deletedRuleFromNeedsReviewInterview = autoAssessedRulesFromNeedsReviewInterview.get(0);
        assertEquals(1L, deletedRuleFromNeedsReviewInterview.getIdRule());
        assertEquals(3, deletedRuleFromNeedsReviewInterview.getLevelValue());
        assertEquals(Integer.valueOf(1), deletedRuleFromNeedsReviewInterview.getDeleted());
        assertNotNull(deletedRuleFromNeedsReviewInterview.getAgent());
        RuleVO activeRuleFromNeedsReviewInterview = autoAssessedRulesFromNeedsReviewInterview.get(1);
        assertEquals(0L, activeRuleFromNeedsReviewInterview.getIdRule());
        assertEquals(3, activeRuleFromNeedsReviewInterview.getLevelValue());
        assertEquals(Integer.valueOf(0),activeRuleFromNeedsReviewInterview.getDeleted());
        assertNull(activeRuleFromNeedsReviewInterview.getAgent());
    }

    @Test
    void givenInterview_whenDetermineFiredRules_shouldPopulateFiredRules() {
        InterviewVO interview = buildSampleInterview(1L, AssessmentStatusEnum.NOTASSESSED);
        when(interviewAnswerService.findByInterviewId(interview.getInterviewId())).thenReturn(buildSampleInterviewAnswers(interview));
        when(moduleRuleService.findByIdNode(anyLong())).thenReturn(buildSampleModuleRules(1L));
        when(manualAssessmentService.findByInterviewId(interview.getInterviewId())).thenReturn(buildManualAssessedRules(interview));
        doNothing().when(interviewService).update(any());

        InterviewVO enrichedInterview = interviewService.determineFiredRules(interview);

        assertNotNull(enrichedInterview);
        assertEquals(interview.getInterviewId(), enrichedInterview.getInterviewId());
        assertEquals(interview.getAssessedStatus(), enrichedInterview.getAssessedStatus());
        assertNotNull(enrichedInterview.getFiredRules());
        assertEquals(1, enrichedInterview.getFiredRules().size());
        assertEquals(1, enrichedInterview.getManualAssessedRules().size());
        assertEquals(1, enrichedInterview.getNotes().size());
        assertNull(enrichedInterview.getAutoAssessedRules());
        RuleVO firedRule = enrichedInterview.getFiredRules().get(0);
        assertEquals(1L, firedRule.getIdRule());
        assertEquals(Integer.valueOf(0), firedRule.getDeleted());
        assertEquals(3, firedRule.getLevelValue());
        assertNotNull(firedRule.getConditions());
        assertEquals(1, firedRule.getConditions().size());
        PossibleAnswerVO possibleAnswer = firedRule.getConditions().get(0);
        assertEquals(1L, possibleAnswer.getIdNode());
        RuleVO manualAssessedRule = enrichedInterview.getManualAssessedRules().get(0);
        assertEquals(1L, manualAssessedRule.getIdRule());
        assertEquals(5, manualAssessedRule.getLevelValue());
        assertEquals(1, manualAssessedRule.getConditions().size());
        assertEquals(Integer.valueOf(0), manualAssessedRule.getDeleted());
        NoteVO note = enrichedInterview.getNotes().get(0);
        assertEquals(1L, note.getInterviewId());
        assertEquals("System", note.getType());
        assertEquals("Ran determineFiredRules", note.getText());
    }

    private List<InterviewManualAssessmentVO> buildManualAssessedRules(
            InterviewVO interview) {
        List<InterviewManualAssessmentVO> manualAssessments
                = new ArrayList<>();
        InterviewManualAssessmentVO manualAssessment 
                = new InterviewManualAssessmentVO();
        manualAssessment.setIdInterview(interview.getInterviewId());
        manualAssessment.setRule(buildRule(5));
        manualAssessment.setId(1L);
        manualAssessments.add(manualAssessment);
        return manualAssessments;
    }

    private List<InterviewVO> buildSampleInterviewVOs(long id, AssessmentStatusEnum assessmentStatusEnum) {
        List<InterviewVO> interviews = new ArrayList<>();
        InterviewVO interview = buildSampleInterview(id, assessmentStatusEnum);
        interviews.add(interview);
        return interviews;
    }

    private InterviewVO buildSampleInterview(long id, AssessmentStatusEnum assessmentStatusEnum) {
        InterviewVO interview = new InterviewVO();
        interview.setInterviewId(id);
        interview.setReferenceNumber("FARM_4008");
        interview.setParentId(0L);
        interview.setAssessedStatus(assessmentStatusEnum.getDisplay());
        return interview;
    }

    List<Interview> buildSampleInterviews(long id){
        List<Interview> interviews = new ArrayList<>();
        Interview interview = new Interview();
        interview.setIdinterview(id);
        interview.setReferenceNumber("FARM_4008");
        interview.setParentId(0L);
        interview.setAssessedStatus("Needs Review");
        interviews.add(interview);
        return interviews;
    }

    List<ModuleRuleVO> buildSampleModuleRules(long answerId){
        List<ModuleRuleVO> moduleRules = new ArrayList<>();
        ModuleRuleVO moduleRule = buildModuleRule(answerId);
        moduleRules.add(moduleRule);
        return moduleRules;
    }

    private ModuleRuleVO buildModuleRule(long answerId) {
        ModuleRuleVO moduleRule = new ModuleRuleVO();
        moduleRule.setIdModule(1L);
        moduleRule.setModuleName("Sample Module");
        moduleRule.setIdRule(1L);
        moduleRule.setRuleLevel("3");
        moduleRule.setIdAgent(1L);
        moduleRule.setAgentName("Biological enzymes");
        moduleRule.setIdNode(answerId);
        moduleRule.setNodeNumber("1");
        moduleRule.setRule(buildRule(3));
        return moduleRule;
    }

    List<InterviewAnswerVO> buildSampleInterviewAnswers(InterviewVO interview){
        List<InterviewAnswerVO> interviewAnswers = new ArrayList<>();
        InterviewAnswerVO answer = buildAnswer(interview);
        interviewAnswers.add(answer);
        return interviewAnswers;
    }

    private InterviewAnswerVO buildAnswer(InterviewVO interview) {
        InterviewAnswerVO answer = new InterviewAnswerVO();
        answer.setId(1L);
        answer.setIdInterview(interview.getInterviewId());
        answer.setTopNodeId(1L);
        answer.setParentQuestionId(1L);
        answer.setAnswerId(1L);
        answer.setLink(0L);
        answer.setName("In professional, scientific services, healthcare, education or administration");
        answer.setIsProcessed(true);
        answer.setModCount(1);
        answer.setNodeClass("P");
        answer.setNumber("7A");
        answer.setType("P_simple");
        answer.setDeleted(0);
        answer.setInterviewQuestionId(1);
        return answer;
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

    List<InterviewAutoAssessmentVO> buildSampleAutoAssessment(int level){
        List<InterviewAutoAssessmentVO> interviewAutoAssessments
                = new ArrayList<>();
        InterviewAutoAssessmentVO interviewAutoAssessment
                = new InterviewAutoAssessmentVO();
        interviewAutoAssessment.setIdInterview(1L);
        interviewAutoAssessment.setId(1L);
        interviewAutoAssessment.setRule(buildRule(level));
        interviewAutoAssessments.add(interviewAutoAssessment);
        return interviewAutoAssessments;
    }

    List<InterviewFiredRulesVO> buildInterviewFiredRules(int level){
        List<InterviewFiredRulesVO> interviewFiredRules
            = new ArrayList<>();
        InterviewFiredRulesVO interviewFiredRule =
                new InterviewFiredRulesVO();
        interviewFiredRule.setId(1L);
        interviewFiredRule.setIdRule(1L);
        interviewFiredRule.setIdinterview(10L);
        interviewFiredRule.setRules(Collections.singletonList(buildRule(level)));
        interviewFiredRules.add(interviewFiredRule);
        return interviewFiredRules;
    }

    RuleVO buildRule(int level){
        RuleVO rule = new RuleVO();
        rule.setIdRule(1L);
        rule.setDeleted(0);
        rule.setLevel("5");
        rule.setLevelValue(level);
        rule.setAgentId(72);
        rule.setAgent(buildAgent());
        rule.setConditions(buildPossibleAnswers());
        return rule;
    }

    List<PossibleAnswerVO> buildPossibleAnswers() {
        List<PossibleAnswerVO> possibleAnswers =
                new ArrayList<>();
        PossibleAnswerVO possibleAnswer = buildPossibleAnswer();
        possibleAnswers.add(possibleAnswer);
        return possibleAnswers;
    }

    private PossibleAnswerVO buildPossibleAnswer() {
        PossibleAnswerVO possibleAnswer =
                new PossibleAnswerVO();
        possibleAnswer.setIdNode(1L);
        possibleAnswer.setDeleted(0);
        possibleAnswer.setDescription("Answer A");
        possibleAnswer.setLink(0);
        possibleAnswer.setName("Answer A");
        possibleAnswer.setNodeclass("P");
        possibleAnswer.setNumber("1A1A");
        possibleAnswer.setOriginalId(0);
        possibleAnswer.setSequence(0);
        possibleAnswer.setTopNodeId(1L);
        possibleAnswer.setType("P_simple");
        possibleAnswer.setParentId("1");
        return possibleAnswer;
    }


}