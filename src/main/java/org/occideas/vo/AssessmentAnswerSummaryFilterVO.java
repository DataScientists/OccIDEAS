package org.occideas.vo;

public class AssessmentAnswerSummaryFilterVO {

	private Long answerId;
	private String name;
	private String moduleName;
	private Long idParticipant;
	private String reference;
	private Long idinterview;
	private String assessedStatus;
	private String statusDescription;
	private Integer status;
	private int pageNumber;
	private int size;

	public Long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Long getIdParticipant() {
		return idParticipant;
	}

	public void setIdParticipant(Long idParticipant) {
		this.idParticipant = idParticipant;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Long getIdinterview() {
		return idinterview;
	}

	public void setIdinterview(Long idinterview) {
		this.idinterview = idinterview;
	}

	public String getAssessedStatus() {
		return assessedStatus;
	}

	public void setAssessedStatus(String assessedStatus) {
		this.assessedStatus = assessedStatus;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus() {
		if ("Running".equals(statusDescription)) {
			this.status = 0;
			return;
		} else if ("Partial".equals(statusDescription)) {
			this.status = 1;
			return;
		} else if ("Completed".equals(statusDescription)) {
			this.status = 2;
			return;
		} else if("To be excluded".equals(statusDescription)){
			this.status = 3;
			return;
		}
		this.status = null;
	}

}
