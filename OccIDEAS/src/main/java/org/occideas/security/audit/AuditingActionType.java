package org.occideas.security.audit;

public enum AuditingActionType {

	ADD_PARTICIPANT("Add Participant"),
	START_INTERVIEW("Run Interview"),
	CONTINUE_INTERVIEW("Continue Interview");

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
