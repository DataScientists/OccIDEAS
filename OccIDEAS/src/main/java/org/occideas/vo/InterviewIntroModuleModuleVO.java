package org.occideas.vo;

public class InterviewIntroModuleModuleVO {

	private String primaryKey;
	private long idModule;
	private String introModuleNodeName;
	private long interviewId;
	private String interviewModuleName;

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

}
