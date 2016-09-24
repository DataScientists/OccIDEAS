package org.occideas.utilities;

public enum ReportsStatusEnum {

	IN_PROGRESS("In Progress"), 
	FAILED("Failed"),
	COMPLETED("Completed");
	
	private ReportsStatusEnum(String value) {
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
