package org.occideas.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;


@Entity
@DynamicUpdate(value=true)
@DynamicInsert(value=true)
@SelectBeforeUpdate(value=true)
@Table(name = "Interview_Question_Answer")
public class InterviewQuestionAnswer implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue
    private long id;
    @Column(name = "interview_idinterview")
    private long idInterview;
    @OneToMany(targetEntity=InterviewQuestion.class,fetch=FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name="question_id",referencedColumnName="question_id"),
        @JoinColumn(name="idinterview", referencedColumnName="interview_idinterview")
    })
    private List<InterviewQuestion> question;
    @Column(name="question_id")
    private long questionId;
    @Column(name = "sequence")
    private int sequence;
    private int deleted;

    public InterviewQuestionAnswer() {
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

	public List<InterviewQuestion> getQuestion() {
		return question;
	}

	public void setQuestion(List<InterviewQuestion> question) {
		this.question = question;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdInterview() {
		return idInterview;
	}

	public void setIdInterview(long idInterview) {
		this.idInterview = idInterview;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	
	
	
}
