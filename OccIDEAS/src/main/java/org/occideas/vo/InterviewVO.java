package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by quangnn on 2/17/2016.
 */

public class InterviewVO {
	private String referenceNumber;
	private String freeText;
    private long singleAnswerId;
    private List<Long> multipleAnswerId;
    private long interviewId;
    private ModuleVO module;
    private FragmentVO fragment;
    private boolean active;
    
    private List<InterviewQuestionAnswerVO> questionsAsked;
    
    private List<RuleVO> firedRules;
    
    private List<AgentVO> agents;
    
    private long questionId;

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

	public List<InterviewQuestionAnswerVO> getQuestionsAsked() {
		if(questionsAsked==null){
			questionsAsked = new ArrayList<InterviewQuestionAnswerVO>();
		}
		return questionsAsked;
	}

	public void setQuestionsAsked(List<InterviewQuestionAnswerVO> questionsAsked) {
		this.questionsAsked = questionsAsked;
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
		for(RuleVO rule:this.getFiredRules()){
			AgentVO agent = new AgentVO();
			agent.setIdAgent(rule.getAgentId());
			if(!(agents.contains(agent))){
				agents.add(agent);
			}
		}
		return agents;
	}

	public void setAgents(List<AgentVO> agents) {
		this.agents = agents;
	}
	
}
