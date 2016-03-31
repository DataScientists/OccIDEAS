package org.occideas.vo;

import java.util.List;

/**
 * Created by quangnn on 2/17/2016.
 */

public class InterviewQuestionAnswerVO{
	private long id;
    private long idInterview;
    private List<InterviewQuestionVO> question;
    private int deleted;
       
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public List<InterviewQuestionVO> getQuestion() {
		return question;
	}
	
	public void setQuestion(List<InterviewQuestionVO> question) {
		this.question = question;
	}
	
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	public long getIdInterview() {
		return idInterview;
	}
	public void setIdInterview(long idInterview) {
		this.idInterview = idInterview;
	}
}
