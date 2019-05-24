package org.occideas.utilities;

public enum ParticipantsStatusEnum {

  RUNNING(0, "Running"),
  PARTIAL(1, "Partial"),
  COMPLETED(2, "Completed"),
  TOBEEXCLUDED(3, "To be excluded");

  private int status;
  private String display;
  ParticipantsStatusEnum(int status, String display) {
    this.status = status;
    this.display = display;
  }

  public static String getDisplayByStatus(int status) {
    String display = "Invalid Status";
    for (ParticipantsStatusEnum enums : ParticipantsStatusEnum.values()) {
      if (enums.getStatus() == status) {
        display = enums.getDisplay();
        break;
      }
    }
    return display;
  }

  public static int getStatusByDisplay(String display) {
    int results = 0;
    for (ParticipantsStatusEnum enums : ParticipantsStatusEnum.values()) {
      if (enums.getDisplay().equals(display)) {
        results = enums.getStatus();
        break;
      }
    }
    return results;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

}
