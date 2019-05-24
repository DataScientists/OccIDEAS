package org.occideas.security.audit;

public enum AuditingActionType {

  ADD_PARTICIPANT("Add Participant"),
  START_INTERVIEW("Run Interview"),
  CONTINUE_INTERVIEW("Continue Interview"),
  DELETE_PARTICIPANT("Delete Participant"),
  CREATE_INTERVIEW("Create Interview"),
  ADD_INTERVIEW_QUESTION("Add Interview Question"),
  ADD_INTERVIEW_ANSWER("Add Interview Answer"),
  UPD_INTERVIEW_QUESTION("Update Interview Question"),
  DEL_INTERVIEW_QUESTION("Delete Interview Question"),
  CREATE_QUESTION("Create Question"),
  UPDATE_QUESTION("Update Question"),
  DEL_QUESTION("Delete Question"),
  CREATE_MODULE("Create Module"),
  UPDATE_MODULE("Update Module"),
  DEL_MODULE("Delete Module"),
  CREATE_RULE("Create Rule"),
  SAVE_UPD_RULE("Save Rule"),
  GENERIC("Generic"),
  DELETE_RULE("Delete Rule");


  private String value;

  AuditingActionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
