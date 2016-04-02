package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by quangnn on 2/17/2016.
 */

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

	private List<RuleVO> firedRules;

	private List<RuleVO> autoAssessedRules;

	private List<RuleVO> manualAssessedRules;

	private List<AgentVO> agents;

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
		if (this.getManualAssessedRules() != null) {
			if (this.getManualAssessedRules().size() > 0) {
				assessedStatus = "Complete";
			} else {
				assessedStatus = "Incomplete";
			}
		}
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

}
