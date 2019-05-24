package org.occideas.utilities;

public enum ReportsEnum {

  REPORT_INTERVIEW_EXPORT("Interviews (Export)"),
  REPORT_ASSESSMENT_EXPORT("Assessments (Export Assessments)"),
  REPORT_NOISE_ASSESSMENT_EXPORT("Noise_Assessment (Export Noise Assessments)"),
  REPORT_MODULE_JSON_EXPORT("Module JSON (Export)"),
  REPORT_VIBRATION_ASSESSMENT_EXPORT("Vibration_Assessment (Export Vibration Assessments)"),
  NOTES_EXPORT("Notes (Export)"),
  INTERVIEW_FIREDRULES("Interview Fired Rules");

  private String value;

  ReportsEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


}
