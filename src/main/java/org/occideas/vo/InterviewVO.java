package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangnn on 2/17/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterviewVO {
  private final String INTRO_MODULE = "M_IntroModule";

  private String referenceNumber;
  private String freeText;
  private long singleAnswerId;
  private List<Long> multipleAnswerId;
  private long interviewId;
  private ModuleVO module;
  private FragmentVO fragment;
  private boolean active;
  private String moduleName;
  private String interviewType;
  private String assessedStatus;
  private ParticipantVO participant;

  private List<InterviewQuestionVO> actualQuestion = new ArrayList<>();

  private List<InterviewQuestionVO> questionHistory = new ArrayList<>();

  private List<InterviewQuestionVO> questionQueueUnprocessed = new ArrayList<>();

  private List<InterviewAnswerVO> answerHistory = new ArrayList<>();

  private List<InterviewModuleVO> modules = new ArrayList<>();

  private List<InterviewIntroModuleModuleVO> moduleList = new ArrayList<>();

  private List<RuleVO> firedRules;

  private List<RuleVO> autoAssessedRules;

  private List<RuleVO> manualAssessedRules;

  private List<AgentVO> agents;

  private List<NoteVO> notes;

  @JsonInclude(Include.NON_NULL)
  private List<InterviewVO> interviews;

  private long questionId;

  private long parentId;

  public long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(long questionId) {
    this.questionId = questionId;
  }

  public ModuleVO getModule() {
    return module;
  }

  public void setModule(ModuleVO module) {
    this.module = module;
  }

  public FragmentVO getFragment() {
    return fragment;
  }

  public void setFragment(FragmentVO fragment) {
    this.fragment = fragment;
  }

  public String getFreeText() {
    return freeText;
  }

  public void setFreeText(String freeText) {
    this.freeText = freeText;
  }

  public long getSingleAnswerId() {
    return singleAnswerId;
  }

  public void setSingleAnswerId(long singleAnswerId) {
    this.singleAnswerId = singleAnswerId;
  }

  public List<Long> getMultipleAnswerId() {
    return multipleAnswerId;
  }

  public void setMultipleAnswerId(List<Long> multipleAnswerId) {
    this.multipleAnswerId = multipleAnswerId;
  }

  public long getInterviewId() {
    return interviewId;
  }

  public void setInterviewId(long interviewId) {
    this.interviewId = interviewId;
  }

  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public List<InterviewQuestionVO> getActualQuestion() {
    return actualQuestion;
  }

  public void setActualQuestion(List<InterviewQuestionVO> actualQuestion) {
    this.actualQuestion = actualQuestion;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<RuleVO> getFiredRules() {
    return firedRules;
  }

  public void setFiredRules(List<RuleVO> firedRules) {
    this.firedRules = firedRules;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public List<AgentVO> getAgents() {
    agents = new ArrayList<AgentVO>();
    if (this.getFiredRules() != null) {
      for (RuleVO rule : this.getFiredRules()) {
        AgentVO agent = rule.getAgent();
        if (!(agents.contains(agent))) {
          agents.add(agent);
        }
      }
    }
    return agents;
  }

  public void setAgents(List<AgentVO> agents) {
    this.agents = agents;
  }

  public String getModuleName() {
    if (this.getModule() != null) {
      moduleName = this.getModule().getName();
    } else if (this.getFragment() != null) {
      moduleName = this.getFragment().getName();
    }
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public String getInterviewType() {
    if (this.getModule() != null) {
      if (this.getModule().getType().equalsIgnoreCase(INTRO_MODULE)) {
        interviewType = "Introduction Module";
      } else {
        interviewType = "Job Module";
      }
    } else if (this.getFragment() != null) {
      interviewType = "Associate Job Module";
    }
    return interviewType;
  }

  public void setInterviewType(String interviewType) {
    this.interviewType = interviewType;
  }

  public List<RuleVO> getAutoAssessedRules() {
    return autoAssessedRules;
  }

  public void setAutoAssessedRules(List<RuleVO> autoAssessedRules) {
    this.autoAssessedRules = autoAssessedRules;
  }

  public List<RuleVO> getManualAssessedRules() {
    return manualAssessedRules;
  }

  public void setManualAssessedRules(List<RuleVO> manualAssessedRules) {
    this.manualAssessedRules = manualAssessedRules;
  }

  public String getAssessedStatus() {
    return assessedStatus;
  }

  public void setAssessedStatus(String assessedStatus) {
    this.assessedStatus = assessedStatus;
  }

  public ParticipantVO getParticipant() {
    return participant;
  }

  public void setParticipant(ParticipantVO participant) {
    this.participant = participant;
  }

  public List<InterviewVO> getInterviews() {
    if (interviews == null) {
      interviews = new ArrayList<InterviewVO>();
    }
    return interviews;
  }

  public void setInterviews(List<InterviewVO> interviews) {
    this.interviews = interviews;
  }

  public long getParentId() {
    return parentId;
  }

  public void setParentId(long parentId) {
    this.parentId = parentId;
  }

  public List<NoteVO> getNotes() {
    if (notes == null) {
      notes = new ArrayList<NoteVO>();
    }
    return notes;
  }

  public void setNotes(List<NoteVO> notes) {
    this.notes = notes;
  }

  public List<InterviewModuleVO> getModules() {
    return modules;
  }

  public void setModules(List<InterviewModuleVO> modules) {
    this.modules = modules;
  }

  public List<InterviewQuestionVO> getQuestionHistory() {
    return questionHistory;
  }

  public void setQuestionHistory(List<InterviewQuestionVO> questionHistory) {
    this.questionHistory = questionHistory;
  }

  public List<InterviewAnswerVO> getAnswerHistory() {
    return answerHistory;
  }

  public void setAnswerHistory(List<InterviewAnswerVO> answerHistory) {
    this.answerHistory = answerHistory;
  }

  public List<InterviewQuestionVO> getQuestionQueueUnprocessed() {
    return questionQueueUnprocessed;
  }

  public void setQuestionQueueUnprocessed(List<InterviewQuestionVO> questionQueueUnprocessed) {
    this.questionQueueUnprocessed = questionQueueUnprocessed;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (interviewId ^ (interviewId >>> 32));
    result = prime * result + ((referenceNumber == null) ? 0 : referenceNumber.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    InterviewVO other = (InterviewVO) obj;
    if (interviewId != other.interviewId)
      return false;
    if (interviewId == other.interviewId)
      return true;
    if (referenceNumber == null) {
      return other.referenceNumber == null;
    } else return referenceNumber.equals(other.referenceNumber);
  }

  public List<InterviewIntroModuleModuleVO> getModuleList() {
    return moduleList;
  }

  public void setModuleList(List<InterviewIntroModuleModuleVO> moduleList) {
    this.moduleList = moduleList;
  }

}
