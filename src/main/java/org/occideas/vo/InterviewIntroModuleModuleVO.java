package org.occideas.vo;

public class InterviewIntroModuleModuleVO {

	private String primaryKey;
	private long idModule;
	private String introModuleNodeName;
	private long interviewId;
	private String interviewModuleName;
	private long interviewPrimaryKey;
	private String linkName;
	private Long linkId;

	public long getIdModule() {
		return idModule;
	}

	public void setIdModule(long idModule) {
		this.idModule = idModule;
	}

	public String getIntroModuleNodeName() {
		return introModuleNodeName;
	}

	public void setIntroModuleNodeName(String introModuleNodeName) {
		this.introModuleNodeName = introModuleNodeName;
	}

	public long getInterviewId() {
		return interviewId;
	}

	public void setInterviewId(long interviewId) {
		this.interviewId = interviewId;
	}

	public String getInterviewModuleName() {
		return interviewModuleName;
	}

	public void setInterviewModuleName(String interviewModuleName) {
		this.interviewModuleName = interviewModuleName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public long getInterviewPrimaryKey() {
		return interviewPrimaryKey;
	}

	public void setInterviewPrimaryKey(long interviewPrimaryKey) {
		this.interviewPrimaryKey = interviewPrimaryKey;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

}
