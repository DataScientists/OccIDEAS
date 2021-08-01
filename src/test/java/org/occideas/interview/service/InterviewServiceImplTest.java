package org.occideas.interview.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.agent.dao.IAgentDao;
import org.occideas.entity.*;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.interviewanswer.dao.InterviewAnswerDao;
import org.occideas.interviewfiredrules.dao.InterviewFiredRulesDao;
import org.occideas.interviewmanualassessment.dao.InterviewManualAssessmentDao;
import org.occideas.modulerule.dao.ModuleRuleDao;
import org.occideas.utilities.AssessmentStatusEnum;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.occideas.utilities.AssessmentStatusEnum.*;

@ExtendWith(MockitoExtension.class)
class InterviewServiceImplTest {

    @Mock
    InterviewDao interviewDao;
    @Mock
    IAgentDao agentDao;
    @Mock
    InterviewAnswerDao interviewAnswerDao;
    @Mock
    ModuleRuleDao moduleRuleDao;
    @Mock
    InterviewManualAssessmentDao interviewManualAssessmentDao;
    @Mock
    InterviewFiredRulesDao interviewFiredRulesDao;
    @Mock
    PlatformTransactionManager platformTransactionManager;

    @Spy
    @InjectMocks
    InterviewServiceImpl interviewService;

    @Test
    void givenListOfInterviews_whenAutoAssessedRules_shouldReturnNoExposure() {
        Interview processInterview = buildSampleInterviewEntity(1L, NEEDSREVIEW);
        doReturn(Collections.singletonList(new Rule())).when(interviewService).determineFiredRules(any());

        interviewService.autoAssessedRule(buildSampleAgents()
                        .stream().map(AgentInfo::getIdAgent).collect(Collectors.toList()),
                processInterview);

        assertEquals(NEEDSREVIEW.getDisplay(), processInterview.getAssessedStatus());
        List<Rule> autoAssessedRulesFromNeedsReviewInterview = processInterview.getAutoAssessedRules();
        assertNotNull(autoAssessedRulesFromNeedsReviewInterview);
    }

    @Test
    void givenListOfInterviews_whenAutoAssessedRules_shouldReturnExposure() {
        Interview processInterview = buildSampleInterviewEntity(1L, NOTASSESSED);
        doReturn(Collections.singletonList(new Rule())).when(interviewService).determineFiredRules(any());

        interviewService.autoAssessedRule(buildSampleAgents()
                        .stream().map(AgentInfo::getIdAgent).collect(Collectors.toList()),
                processInterview);

        assertEquals(AUTOASSESSED.getDisplay(), processInterview.getAssessedStatus());
        List<Rule> autoAssessedRulesFromNeedsReviewInterview = processInterview.getAutoAssessedRules();
        assertNotNull(autoAssessedRulesFromNeedsReviewInterview);
    }

    @Test
    void givenInterview_whenDetermineFiredRules_shouldPopulateFiredRules() {
        Interview interview = buildSampleInterviewEntity(1L, AssessmentStatusEnum.NOTASSESSED);
        Interview enrichedInterview = buildSampleInterviewEntity(1L, AssessmentStatusEnum.NOTASSESSED);
        enrichedInterview.setAnswerHistory(buildSampleInterviewAnswers(interview));
        when(moduleRuleDao.getRulesByUniqueAnswers(any())).thenReturn(buildRules(3));

        interviewService.determineFiredRules(enrichedInterview);

        assertNotNull(enrichedInterview);
        assertEquals(interview.getIdinterview(), enrichedInterview.getIdinterview());
        assertEquals(interview.getAssessedStatus(), enrichedInterview.getAssessedStatus());
    }

    @Test
    void givenRulesWithMultiCondition_whenDeriveFiredRulesByAnswersProvided_shouldPopulateFiredRules() {
        Interview interview = buildSampleInterviewEntity(1L, AssessmentStatusEnum.NOTASSESSED);
        Interview enrichedInterview = buildSampleInterviewEntity(1L, AssessmentStatusEnum.NOTASSESSED);
        enrichedInterview.setAnswerHistory(buildSampleInterviewAnswers(interview));
        when(moduleRuleDao.getRulesByUniqueAnswers(any())).thenReturn(buildRules(3));

        interviewService.determineFiredRules(enrichedInterview);

        assertNotNull(enrichedInterview);
        assertEquals(interview.getIdinterview(), enrichedInterview.getIdinterview());
        assertEquals(interview.getAssessedStatus(), enrichedInterview.getAssessedStatus());
    }



    @Test
    void givenMultipleConditions_whenDeriveFiredRulesByAnswersProvided_shouldReturnFiredRule() {
        List<PossibleAnswer> conditions = new ArrayList<>();
        PossibleAnswer possibleAnswer1 = buildPossibleAnswer(1L);
        PossibleAnswer possibleAnswer2 = buildPossibleAnswer(2L);
        conditions.add(possibleAnswer1);
        conditions.add(possibleAnswer2);
        List<Rule> rules = buildRules(3);
        rules.get(0).setConditions(conditions);
        when(moduleRuleDao.getRulesByUniqueAnswers(any())).thenReturn(rules);
        Set<Long> actualAnswers = new HashSet<>();
        actualAnswers.add(possibleAnswer1.getIdNode());
        actualAnswers.add(possibleAnswer2.getIdNode());

        List<Rule> interviewFiredRules = interviewService.deriveFiredRulesByAnswersProvided(actualAnswers, 1L);

        assertEquals(1, interviewFiredRules.size());
    }

    @Test
    void givenMultipleConditions_whenDeriveFiredRulesByAnswersProvided_shouldReturnFiredRuleAndUnique() {
        List<PossibleAnswer> conditions = new ArrayList<>();
        PossibleAnswer possibleAnswer1 = buildPossibleAnswer(1L);
        PossibleAnswer possibleAnswer2 = buildPossibleAnswer(2L);
        conditions.add(possibleAnswer1);
        conditions.add(possibleAnswer2);
        List<Rule> rules = buildRules(3);
        rules.get(0).setConditions(conditions);
        rules.add(buildRule(1));
        rules.get(1).setConditions(conditions);
        when(moduleRuleDao.getRulesByUniqueAnswers(any())).thenReturn(rules);
        Set<Long> actualAnswers = new HashSet<>();
        actualAnswers.add(possibleAnswer1.getIdNode());
        actualAnswers.add(possibleAnswer2.getIdNode());

        List<Rule> interviewFiredRules = interviewService.deriveFiredRulesByAnswersProvided(actualAnswers, 1L);

        assertEquals(1, interviewFiredRules.size());
    }

    @Test
    void givenMultipleConditions_whenDeriveFiredRulesByAnswersProvided_shouldNotReturnFiredRule() {
        List<PossibleAnswer> conditions = new ArrayList<>();
        PossibleAnswer possibleAnswer1 = buildPossibleAnswer(1L);
        PossibleAnswer possibleAnswer2 = buildPossibleAnswer(2L);
        conditions.add(possibleAnswer1);
        conditions.add(possibleAnswer2);
        List<Rule> rules = buildRules(3);
        rules.get(0).setConditions(conditions);
        when(moduleRuleDao.getRulesByUniqueAnswers(any())).thenReturn(rules);
        Set<Long> actualAnswers = new HashSet<>();
        actualAnswers.add(possibleAnswer1.getIdNode());

        List<Rule> interviewFiredRules = interviewService.deriveFiredRulesByAnswersProvided(actualAnswers, 1L);

        assertTrue(interviewFiredRules.isEmpty());
    }


    private List<InterviewManualAssessment> buildManualAssessedRules(
            Interview interview) {
        List<InterviewManualAssessment> manualAssessments
                = new ArrayList<>();
        InterviewManualAssessment manualAssessment
                = new InterviewManualAssessment();
        manualAssessment.setIdInterview(interview.getIdinterview());
        manualAssessment.setRule(buildRule(5));
        manualAssessment.setId(1L);
        manualAssessments.add(manualAssessment);
        return manualAssessments;
    }

    List<Interview> buildSampleInterviews(long id, AssessmentStatusEnum assessmentStatusEnum){
        List<Interview> interviews = new ArrayList<>();
        Interview interview = buildSampleInterviewEntity(id, assessmentStatusEnum);
        interviews.add(interview);
        return interviews;
    }

    private Interview buildSampleInterviewEntity(long id, AssessmentStatusEnum assessmentStatusEnum) {
        Interview interview = new Interview();
        interview.setIdinterview(id);
        interview.setReferenceNumber("FARM_4008");
        interview.setParentId(0L);
        interview.setAssessedStatus(assessmentStatusEnum.getDisplay());
        return interview;
    }

    List<ModuleRule> buildSampleModuleRules(long answerId){
        List<ModuleRule> moduleRules = new ArrayList<>();
        ModuleRule moduleRule = buildModuleRule(answerId);
        moduleRules.add(moduleRule);
        return moduleRules;
    }

    private ModuleRule buildModuleRule(long answerId) {
        ModuleRule moduleRule = new ModuleRule();
        moduleRule.setIdModule(1L);
        moduleRule.setModuleName("Sample Module");
        moduleRule.setIdRule("1");
        moduleRule.setRuleLevel("3");
        moduleRule.setIdAgent(1L);
        moduleRule.setAgentName("Biological enzymes");
        moduleRule.setIdNode(answerId);
        moduleRule.setNodeNumber("1");
        moduleRule.setRule(buildRule(3));
        return moduleRule;
    }

    List<InterviewAnswer> buildSampleInterviewAnswers(Interview interview){
        List<InterviewAnswer> interviewAnswers = new ArrayList<>();
        InterviewAnswer answer = buildAnswer(interview);
        interviewAnswers.add(answer);
        return interviewAnswers;
    }

    private InterviewAnswer buildAnswer(Interview interview) {
        InterviewAnswer answer = new InterviewAnswer();
        answer.setId(1L);
        answer.setIdInterview(interview.getIdinterview());
        answer.setTopNodeId(1L);
        answer.setParentQuestionId(1L);
        answer.setAnswerId(1L);
        answer.setLink(0L);
        answer.setName("In professional, scientific services, healthcare, education or administration");
        answer.setProcessed(true);
        answer.setModCount(1);
        answer.setNodeClass("P");
        answer.setNumber("7A");
        answer.setType("P_simple");
        answer.setDeleted(0);
        answer.setInterviewQuestionId(1);
        return answer;
    }

    List<Agent> buildSampleAgents(){
        List<Agent> agents = new ArrayList<>();
        Agent agent = buildAgent();
        agents.add(agent);
        return agents;
    }

    private Agent buildAgent() {
        Agent agent = new Agent();
        agent.setIdAgent(72L);
        agent.setDeleted(0);
        agent.setName("1,3 butadiene");
        return agent;
    }

    List<Rule> buildRules(int level){
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setIdRule(1L);
        rule.setDeleted(0);
        rule.setLevel(level);
        rule.setAgentId(72);
        rule.setAgent(buildAgent());
        rule.setConditions(buildPossibleAnswers());
        rules.add(rule);
        return rules;
    }

    Rule buildRule(int level){
        Rule rule = new Rule();
        rule.setIdRule(1L);
        rule.setDeleted(0);
        rule.setLevel(level);
        rule.setAgentId(72);
        rule.setAgent(buildAgent());
        rule.setConditions(buildPossibleAnswers());
        return rule;
    }

    List<PossibleAnswer> buildPossibleAnswers() {
        List<PossibleAnswer> possibleAnswers =
                new ArrayList<>();
        PossibleAnswer possibleAnswer = buildPossibleAnswer(1L);
        possibleAnswers.add(possibleAnswer);
        return possibleAnswers;
    }

    private PossibleAnswer buildPossibleAnswer(long idNode) {
        PossibleAnswer possibleAnswer =
                new PossibleAnswer();
        possibleAnswer.setIdNode(idNode);
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