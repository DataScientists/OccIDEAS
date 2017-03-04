package org.occideas.vo;

import java.util.ArrayList;
import java.util.List;

public class RandomInterviewReport {

	private long interviewId;
	private List<InterviewQuestionVO> listQuestion = new ArrayList<>();
	private List<InterviewAnswerVO> listAnswer = new ArrayList<>();
	private String referenceNumber;

	public long getInterviewId() {
		return interviewId;
	}

	public void setInterviewId(long interviewId) {
		this.interviewId = interviewId;
	}

	public List<InterviewQuestionVO> getListQuestion() {
		return listQuestion;
	}

	public void setListQuestion(List<InterviewQuestionVO> listQuestion) {
		this.listQuestion = listQuestion;
	}

	public List<InterviewAnswerVO> getListAnswer() {
		return listAnswer;
	}

	public void setListAnswer(List<InterviewAnswerVO> listAnswer) {
		this.listAnswer = listAnswer;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

}
