package org.occideas.vo;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.occideas.entity.Interview;

public class ExportCSVVO {

	private Set<String> headers = new LinkedHashSet<>();
	private Set<String> questionIdList = new LinkedHashSet<>();
	private Set<String> questionList = new LinkedHashSet<>();
	private Map<Interview, List<String>> answers = new LinkedHashMap<>();

	public Set<String> getHeaders() {
		return headers;
	}

	public void setHeaders(Set<String> headers) {
		this.headers = headers;
	}

	public Map<Interview, List<String>> getAnswers() {
		return answers;
	}

	public void setAnswers(Map<Interview, List<String>> answers) {
		this.answers = answers;
	}

	public Set<String> getQuestionIdList() {
		return questionIdList;
	}

	public void setQuestionIdList(Set<String> questionIdList) {
		this.questionIdList = questionIdList;
	}

	public Set<String> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(Set<String> questionList) {
		this.questionList = questionList;
	}
}
