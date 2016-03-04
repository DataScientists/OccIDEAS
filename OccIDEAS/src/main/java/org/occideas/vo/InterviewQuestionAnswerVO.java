package org.occideas.vo;

/**
 * Created by quangnn on 2/17/2016.
 */

public class InterviewQuestionAnswerVO implements Comparable<InterviewQuestionAnswerVO>{
	private long id;
	private String interviewQuestionAnswerFreetext;
    private PossibleAnswerVO possibleAnswer;
    private long idInterview;
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
	
	@Override
	public int compareTo(InterviewQuestionAnswerVO o) {
		if((this.getPossibleAnswer()!=null) && (o.getPossibleAnswer()!=null)){
			return this.getPossibleAnswer().compareTo(o.getPossibleAnswer());
		}else{
			return this.getQuestion().compareTo(o.getQuestion());
		}
		
		      
	}
	public long getIdInterview() {
		return idInterview;
	}
	public void setIdInterview(long idInterview) {
		this.idInterview = idInterview;
	}
}
