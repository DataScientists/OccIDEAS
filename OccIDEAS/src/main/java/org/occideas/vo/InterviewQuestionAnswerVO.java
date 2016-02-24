package org.occideas.vo;

/**
 * Created by quangnn on 2/17/2016.
 */

public class InterviewQuestionAnswerVO {
	private long id;
	private String interviewQuestionAnswerfreeText;
    private PossibleAnswerVO possibleAnswer;
    private InterviewVO interview;
    private QuestionVO question;
    private int deleted;
    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getInterviewQuestionAnswerfreeText() {
		return interviewQuestionAnswerfreeText;
	}
	public void setInterviewQuestionAnswerfreeText(String interviewQuestionAnswerfreeText) {
		this.interviewQuestionAnswerfreeText = interviewQuestionAnswerfreeText;
	}
	public PossibleAnswerVO getPossibleAnswer() {
		return possibleAnswer;
	}
	public void setPossibleAnswer(PossibleAnswerVO possibleAnswer) {
		this.possibleAnswer = possibleAnswer;
	}
	public InterviewVO getInterview() {
		return interview;
	}
	public void setInterview(InterviewVO interview) {
		this.interview = interview;
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

}
