package org.occideas.vo;

import java.util.List;

public class InterviewFiredRulesVO {

	private long id;
	private long idinterview;
	private long idRule;
	private List<RuleVO> rules;
	private List<InterviewVO> interviews;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdinterview() {
		return idinterview;
	}

	public void setIdinterview(long idinterview) {
		this.idinterview = idinterview;
	}

	public long getIdRule() {
		return idRule;
	}

	public void setIdRule(long idRule) {
		this.idRule = idRule;
	}

	public List<RuleVO> getRules() {
		return rules;
	}

	public void setRules(List<RuleVO> rules) {
		this.rules = rules;
	}

	public List<InterviewVO> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<InterviewVO> interviews) {
		this.interviews = interviews;
	}

}
