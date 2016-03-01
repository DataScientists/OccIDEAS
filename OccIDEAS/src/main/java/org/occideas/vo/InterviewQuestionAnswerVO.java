package org.occideas.vo;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by quangnn on 2/17/2016.
 */

public class InterviewQuestionAnswerVO implements Comparable<InterviewQuestionAnswerVO>{
	private long id;
	private String interviewQuestionAnswerFreetext;
    private PossibleAnswerVO possibleAnswer;
    private InterviewVO interview;
    private QuestionVO question;
    private int deleted;
    private String referenceNumber;
       
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
	
	public String getReferenceNumber() {
		if(this.referenceNumber==null){
			if(this.getInterview()!=null){
				referenceNumber = this.getInterview().getReferenceNumber();
			}	    
		}
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public InterviewVO getInterview() {
		return interview;
	}
	public void setInterview(InterviewVO interview) {
		this.interview = interview;
	}
	
	@Override
	public int compareTo(InterviewQuestionAnswerVO o) {
		if((this.getPossibleAnswer()!=null) && (o.getPossibleAnswer()!=null)){
			return this.getPossibleAnswer().compareTo(o.getPossibleAnswer());
		}else{
			return this.getQuestion().compareTo(o.getQuestion());
		}
		
		      
	}
}
