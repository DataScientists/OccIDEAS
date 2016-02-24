package org.occideas.vo;

/**
 * Created by quangnn on 2/17/2016.
 */

public class InterviewQuestionAnswerVO {
	private long id;
	private String interviewQuestionAnswerFreetext;
    private PossibleAnswerVO possibleAnswer;
    private long interviewId;
    private QuestionVO question;
    private int deleted;
       
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getInterviewQuestionAnswerFreetext() {
		return interviewQuestionAnswerFreetext;
	}
	public void setInterviewQuestionAnswerFreetext(String interviewQuestionAnswerfreeText) {
		this.interviewQuestionAnswerFreetext = interviewQuestionAnswerfreeText;
	}
	public PossibleAnswerVO getPossibleAnswer() {
		return possibleAnswer;
	}
	public void setPossibleAnswer(PossibleAnswerVO possibleAnswer) {
		this.possibleAnswer = possibleAnswer;
	}
	
	public QuestionVO getQuestion() {
		return question;
	}
	public void setQuestion(QuestionVO question) {
		this.question = question;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	public long getInterviewId() {
		return interviewId;
	}
	public void setInterviewId(long interviewId) {
		this.interviewId = interviewId;
	}

}
