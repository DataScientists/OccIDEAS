package org.occideas.utilities;

public enum ReportsEnum {

	REPORT_INTERVIEW_EXPORT("Interviews (Export)"), 
	REPORT_ASSESSMENT_EXPORT("Assessments (Export Assessments)"),
	REPORT_NOISE_ASSESSMENT_EXPORT("Noise_Assessment (Export Noise Assessments)"),
	REPORT_MODULE_JSON_EXPORT("Module JSON (Export)");
	
	private ReportsEnum(String value) {
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