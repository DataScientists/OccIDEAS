package org.occideas.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ExportCSVVO {

	private Set<String> headers = new TreeSet<>();
	private Set<String> questionIdList = new TreeSet<>();
	private Map<InterviewVO, List<String>> answers = new HashMap<>();

	public Set<String> getHeaders() {
		return headers;
	}

	public void setHeaders(Set<String> headers) {
		this.headers = headers;
	}

	public Map<InterviewVO, List<String>> getAnswers() {
		return answers;
	}

	public void setAnswers(Map<InterviewVO, List<String>> answers) {
		this.answers = answers;
	}

	public Set<String> getQuestionIdList() {
		return questionIdList;
	}

	public void setQuestionIdList(Set<String> questionIdList) {
		this.questionIdList = questionIdList;
	}

}
