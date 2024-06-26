package org.occideas.entity;

import org.hibernate.annotations.CascadeType;
import org.occideas.utilities.AssessmentStatusEnum;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interview generated by hbm2java
 */
@Entity
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
@SelectBeforeUpdate(value = true)
public class Interview implements java.io.Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long idinterview;

  private long parentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "module_idNode", referencedColumnName = "idNode")
  private JobModule module;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fragment_idNode", referencedColumnName = "idNode")
  private Fragment fragment;

  @OneToMany
  @JoinColumn(name = "idinterview", referencedColumnName = "idinterview", insertable = false, updatable = false)
  @Where(clause = "deleted = 0")
  private List<InterviewModule> modules;

  @OneToMany
  @JoinColumn(name = "interviewId", referencedColumnName = "idinterview", insertable = false, updatable = false)
  private List<InterviewIntroModuleModule> moduleList;

  @OneToMany(mappedBy = "interviewId", targetEntity = Note.class)
  @Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.PERSIST})
  @Where(clause = "deleted = 0")
  private List<Note> notes = new ArrayList<>();

  // @OneToMany
  // @JoinColumn(name = "idinterview", referencedColumnName =
  // "idinterview",insertable=false,updatable=false)
  private transient List<InterviewQuestion> actualQuestion;

  @OneToMany
  @JoinColumn(name = "idinterview", referencedColumnName = "idinterview", insertable = false, updatable = false)
  private List<InterviewQuestion> questionHistory;

  @OneToMany
  @Where(clause = "isProcessed = 0")
  @JoinColumn(name = "idinterview", referencedColumnName = "idinterview", insertable = false, updatable = false)
  private List<InterviewQuestion> questionQueueUnprocessed;

  @OneToMany
  @JoinColumn(name = "idinterview", referencedColumnName = "idinterview", insertable = false, updatable = false)
  private List<InterviewAnswer> answerHistory = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "Interview_FiredRules", joinColumns = {
    @JoinColumn(name = "idinterview")}, inverseJoinColumns = {@JoinColumn(name = "idRule")})
  private List<Rule> firedRules;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "Interview_AutoAssessedRules", joinColumns = {
    @JoinColumn(name = "idinterview")}, inverseJoinColumns = {@JoinColumn(name = "idRule")})
  @Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.PERSIST})
  @Where(clause = "deleted = 0")
  private List<Rule> autoAssessedRules;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "Interview_ManualAssessedRules", joinColumns = {
    @JoinColumn(name = "idinterview")}, inverseJoinColumns = {@JoinColumn(name = "idRule")})
  @Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.PERSIST})
  @Where(clause = "deleted = 0")
  private List<Rule> manualAssessedRules;

  private String referenceNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "idParticipant", referencedColumnName = "idParticipant", updatable = false)
  private Participant participant;

  @OneToMany(mappedBy = "parentId", targetEntity = Interview.class)
  @Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.PERSIST})
  private List<Interview> interviews;

  private String assessedStatus;

  public Interview() {
  }

  public Interview(long idinterview) {
    this.idinterview = idinterview;
  }

  public long getIdinterview() {
    return this.idinterview;
  }

  public void setIdinterview(long idinterview) {
    this.idinterview = idinterview;
  }

  public JobModule getModule() {
    return module;
  }

  public void setModule(JobModule module) {
    this.module = module;
  }

  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public Fragment getFragment() {
    return fragment;
  }

  public void setFragment(Fragment fragment) {
    this.fragment = fragment;
  }

  public List<InterviewModule> getModules() {
    return modules;
  }

  public void setModules(List<InterviewModule> modules) {
    this.modules = modules;
  }

  public List<Rule> getFiredRules() {
    return firedRules;
  }

  public void setFiredRules(List<Rule> firedRules) {
    this.firedRules = firedRules;
  }

  public List<Rule> getAutoAssessedRules() {
    return autoAssessedRules;
  }

  public void setAutoAssessedRules(List<Rule> autoAssessedRules) {
    this.autoAssessedRules = autoAssessedRules;
  }

  public List<Rule> getManualAssessedRules() {
    return manualAssessedRules;
  }

  public void setManualAssessedRules(List<Rule> manualAssessedRules) {
    this.manualAssessedRules = manualAssessedRules;
  }

  public Participant getParticipant() {
    return participant;
  }

  public void setParticipant(Participant participant) {
    this.participant = participant;
  }

  public List<Interview> getInterviews() {
    return interviews;
  }

  public void setInterviews(List<Interview> interviews) {
    this.interviews = interviews;
  }

  public long getParentId() {
    return parentId;
  }

  public void setParentId(long parentId) {
    this.parentId = parentId;
  }

  public List<Note> getNotes() {
    return notes;
  }

  public void setNotes(List<Note> notes) {
    this.notes = notes;
  }

  public List<InterviewQuestion> getActualQuestion() {
    return actualQuestion;
  }

  public void setActualQuestion(List<InterviewQuestion> actualQuestion) {
    this.actualQuestion = actualQuestion;
  }

  public List<InterviewQuestion> getQuestionHistory() {
    return questionHistory;
  }

  public void setQuestionHistory(List<InterviewQuestion> questionHistory) {
    this.questionHistory = questionHistory;
  }

  public List<InterviewAnswer> getAnswerHistory() {
    return answerHistory;
  }

  public void setAnswerHistory(List<InterviewAnswer> answerHistory) {
    this.answerHistory = answerHistory;
  }

  public List<InterviewQuestion> getQuestionQueueUnprocessed() {
    return questionQueueUnprocessed;
  }

  public void setQuestionQueueUnprocessed(List<InterviewQuestion> questionQueueUnprocessed) {
    this.questionQueueUnprocessed = questionQueueUnprocessed;
  }

  public String getAssessedStatus() {
	  if(assessedStatus==null) {
		  assessedStatus = AssessmentStatusEnum.NOTASSESSED.getDisplay();
	  }
    return assessedStatus;
  }

  public void setAssessedStatus(String assessedStatus) {
    this.assessedStatus = assessedStatus;
  }

  public List<InterviewIntroModuleModule> getModuleList() {
    return moduleList;
  }

  public void setModuleList(List<InterviewIntroModuleModule> moduleList) {
    this.moduleList = moduleList;
  }

}
