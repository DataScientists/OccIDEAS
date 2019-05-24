package org.occideas.utilities;

public enum ReportsStatusEnum {

  IN_PROGRESS("In Progress"),
  FAILED("Failed"),
  COMPLETED("Completed");

  private String value;

  ReportsStatusEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


}
