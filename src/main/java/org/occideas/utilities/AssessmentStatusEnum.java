package org.occideas.utilities;

public enum AssessmentStatusEnum {

  NOTASSESSED(0, "Not Assessed"),
  AUTOASSESSED(1, "Auto Assessed"),
  INCOMPLETE(3, "Incomplete"),
  NEEDSREVIEW(4, "Needs Review"),
  FINISHED(5, "Finished"),
  MANUALLYASSESSED(6, "Manually assessed");

  private int status;
  private String display;
  AssessmentStatusEnum(int status, String display) {
    this.status = status;
    this.display = display;
  }

  public static String getDisplayByStatus(int status) {
    String display = "Invalid Status";
    for (ProgressStatusEnum enums : ProgressStatusEnum.values()) {
        if (enums.getStatus() == status) {
            display = enums.getDisplay();
            break;
        }
    }
    return display;
  }

  public static int getStatusByDisplay(String display) {
    int results = 0;
      for (ProgressStatusEnum enums : ProgressStatusEnum.values()) {
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
