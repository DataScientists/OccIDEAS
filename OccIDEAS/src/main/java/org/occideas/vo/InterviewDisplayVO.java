package org.occideas.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InterviewDisplayVO {

	private long id;
	private long idInterview;
	private String number;
	private String name;
	private String type;
	private long questionId;
	private Integer sequence;
	private Integer deleted;
	private String header;
	private long parentModuleId;
	private long topNodeId;
	private long parentAnswerId;
	private long link;
	private String description;
	private String nodeClass;
	private List<InterviewDisplayAnswerVO> answers;
	@JsonFormat(pattern="MM/dd/yyyy HH:mm:ss")
	private Date lastUpdated;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public List<InterviewDisplayAnswerVO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<InterviewDisplayAnswerVO> answers) {
		this.answers = answers;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public long getParentModuleId() {
		return parentModuleId;
	}

	public void setParentModuleId(long parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	public long getTopNodeId() {
		return topNodeId;
	}

	public void setTopNodeId(long topNodeId) {
		this.topNodeId = topNodeId;
	}

	public long getParentAnswerId() {
		return parentAnswerId;
	}

	public void setParentAnswerId(long parentAnswerId) {
		this.parentAnswerId = parentAnswerId;
	}

	public long getLink() {
		return link;
	}

	public void setLink(long link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNodeClass() {
		return nodeClass;
	}

	public void setNodeClass(String nodeClass) {
		this.nodeClass = nodeClass;
	}

}
