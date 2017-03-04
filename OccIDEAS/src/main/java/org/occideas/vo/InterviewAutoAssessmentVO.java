package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class InterviewAutoAssessmentVO {

	private long id;
	private long idInterview;
	private RuleVO rule;
	
	public long getIdInterview() {
		return idInterview;
	}

	public void setIdInterview(long idInterview) {
		this.idInterview = idInterview;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public RuleVO getRule() {
		return rule;
	}

	public void setRule(RuleVO rule) {
		this.rule = rule;
	}
	

	@Override
	public String toString() {
		return "InterviewAutoAssessmentVO [id=" + id + ", idInterview=" + idInterview + ", idRule="
				+ rule.getIdRule() + "]";
	}

}
