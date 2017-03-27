package org.occideas.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RandomInterviewVO {

	private Integer count;
	private boolean isRandomAnswers;
	private String[] filterModule;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public boolean isRandomAnswers() {
		return isRandomAnswers;
	}

	public void setIsRandomAnswers(boolean isRandomAnswers) {
		this.isRandomAnswers = isRandomAnswers;
	}

	public String[] getFilterModule() {
		return filterModule;
	}

	public void setFilterModule(String[] filterModule) {
		this.filterModule = filterModule;
	}

}
