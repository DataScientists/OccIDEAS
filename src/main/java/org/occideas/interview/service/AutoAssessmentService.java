package org.occideas.interview.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.*;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.interviewmanualassessment.dao.InterviewManualAssessmentDao;
import org.occideas.modulerule.dao.ModuleRuleDao;
import org.occideas.utilities.AssessmentStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
public class AutoAssessmentService {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private ModuleRuleDao moduleRuleDao;
    @Autowired
    private InterviewDao interviewDao;
    @Autowired
    private InterviewManualAssessmentDao interviewManualAssessmentDao;

    public void deleteOldAutoAssessments(Interview interview) {
        log.info("Start deleting old rules in old assessments for interview {}", interview.getIdinterview());
        interviewDao.deleteAutoAssessment(interview.getIdinterview());
        log.info("Completed deleting old rules in old assessments for interview {}", interview.getIdinterview());
    }

    @Async("autoAssessmentTaskExecutor")
    public CompletableFuture<Interview> autoAssessedRule(List<Long> listAgentIds, Interview interview) {
        log.info("{} processing interview {}", Thread.currentThread().getName(), interview.getIdinterview());
        updateNotes(interview);
        List<Rule> listOfFiredRules = determineFiredRules(interview);
        List<Rule> autoAssessedRules = new ArrayList<>();
        autoAssessedRules.addAll(getRuleLevelNoExposure(
                listAgentIds
                , listOfFiredRules));
        evaluateAssessmentStatus(interview);
        interview.setFiredRules(listOfFiredRules);
        interview.setAutoAssessedRules(autoAssessedRules);
        if (StringUtils.isEmpty(interview.getAssessedStatus())) {
            interview.setAssessedStatus(AssessmentStatusEnum.NOTASSESSED.getDisplay());
        }
        return CompletableFuture.completedFuture(interview);
    }

    public void updateNotes(Interview interview) {
        if (Objects.isNull(interview.getNotes())) {
            interview.setNotes(new ArrayList<>());
        }
        interview.getNotes().add(getDefaultNote(interview.getIdinterview()));
    }

    public List<Rule> determineFiredRules(Interview interview) {
        Set<Long> allActualAnswers = interview.getAnswerHistory()
                .stream()
                .filter(interviewAnswer -> interviewAnswer.getDeleted() == 0)
                .map(InterviewAnswer::getAnswerId)
                .collect(Collectors.toSet());
        return deriveFiredRulesByAnswersProvided(allActualAnswers, interview.getIdinterview());
    }

    public Note getDefaultNote(long interviewId) {
        Note note = new Note();
        note.setInterviewId(interviewId);
        note.setText("Ran determineFiredRules");
        note.setType("System");
        return note;
    }

    public List<Rule> deriveFiredRulesByAnswersProvided(Set<Long> allActualAnswers, long interviewId) {
        List<Rule> derivedRulesBasedOnAnswers = moduleRuleDao.getRulesByUniqueAnswers(allActualAnswers);
        Set<Rule> firedRules = new HashSet<>();
        derivedRulesBasedOnAnswers.stream()
                .filter(rule -> Objects.nonNull(rule.getConditions()))
                .distinct()
                .forEach(rule -> {
                    List<PossibleAnswer> conditions = rule.getConditions();
                    int numberConditionsMet = 0;
                    for (PossibleAnswer answer : conditions) {
                        if (allActualAnswers.contains(answer.getIdNode())) {
                            numberConditionsMet++;
                        }
                    }
                    if (numberConditionsMet == conditions.size()) {
                        firedRules.add(rule);
                    }
                });
        return firedRules.stream().collect(Collectors.toList());
    }

    public List<Rule> getRuleLevelNoExposure(List<Long> listAgentIds, List<Rule> listOfFiredRules) {
        return listAgentIds.stream()
                .map(id -> {
                    if (Objects.nonNull(listOfFiredRules) && !listOfFiredRules.isEmpty()) {
                        Rule lowestLevel = null;
                        for (Rule rule : listOfFiredRules) {
                            if (Objects.isNull(rule) || rule.getAgentId() != id) {
                                continue;
                            }
                            if (Objects.isNull(lowestLevel)) {
                                lowestLevel = rule;
                            }
                            if (rule.getLevel() < lowestLevel.getLevel()) {
                                lowestLevel = rule;
                            }
                        }

                        if (Objects.nonNull(lowestLevel)) {
                            Rule rule = new Rule();
                            rule.setAgentId(id);
                            rule.setLevel(lowestLevel.getLevel());
                            rule.setAgentId(lowestLevel.getAgentId());
                            rule.setLegacyRuleId(lowestLevel.getIdRule());
                            return rule;
                        } else {
                            Rule rule = new Rule();
                            rule.setLevel(5);
                            rule.setAgentId(id);
                            return rule;
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void evaluateAssessmentStatus(Interview interview) {
        if (hasManualAssessedRules(interview)) {
            interview.setAssessedStatus(AssessmentStatusEnum.MANUALLYASSESSED.getDisplay());
        } else {
            if (interview.getAssessedStatus().equalsIgnoreCase(AssessmentStatusEnum.NOTASSESSED.getDisplay())) {
                interview.setAssessedStatus(AssessmentStatusEnum.AUTOASSESSED.getDisplay());
            } else {
                //leave status the same for now
            }

        }
    }

    public boolean hasManualAssessedRules(Interview interview) {
        return interview.getManualAssessedRules() != null &&
                !interview.getManualAssessedRules().isEmpty();
    }

    public void updateManualAssessedRules(Interview interview) {
        List<InterviewManualAssessment> assessmentRules = interviewManualAssessmentDao.findByInterviewId(interview.getIdinterview());
        interview.setManualAssessedRules(assessmentRules
                .stream()
                .map(InterviewManualAssessment::getRule)
                .collect(Collectors.toList()));
    }

    public void deleteOldAutoAssessments() {
        log.info("Start deleting old rules in old assessments");
        interviewDao.deleteAutoAssessments();
        log.info("Completed deleting old rules in old assessments");
    }
}
