package org.occideas.security.audit;

public enum AuditingActionType {

	ADD_PARTICIPANT("Add Participant"),
	START_INTERVIEW("Run Interview"),
	CONTINUE_INTERVIEW("Continue Interview"), 
	DELETE_PARTICIPANT("Delete Participant"), 
	CREATE_INTERVIEW("Create Interview"), 
	ADD_INTERVIEW_QUESTION("Add Interview Question"), 
	UPD_INTERVIEW_QUESTION("Update Interview Question"),
	DEL_INTERVIEW_QUESTION("Delete Interview Question"), 
	CREATE_QUESTION("Create Question"),
	UPDATE_QUESTION("Update Question"),
	DEL_QUESTION("Delete Question"), 
	CREATE_RULE("Create Rule"), 
	SAVE_UPD_RULE("Save Rule"), 
	DELETE_RULE("Delete Rule");

	AuditingActionType(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
