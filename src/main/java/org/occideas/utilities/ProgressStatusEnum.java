package org.occideas.utilities;

public enum ProgressStatusEnum {

  RUNNING(0, "Running"),
  PARTIAL(1, "Partial"),
  COMPLETED(2, "Completed"),
  TOBEEXCLUDED(3, "To be excluded");

  private int status;
  private String display;

  ProgressStatusEnum(int status, String display) {
    this.status = status;
    this.display = display;
  }

  public static String getDisplayByStatus(int status) {
    for (ProgressStatusEnum enums : ProgressStatusEnum.values()) {
      if (enums.getStatus() == status) {
        return enums.getDisplay();
      }
    }
    return RUNNING.getDisplay();
  }

  public static int getStatusByDisplay(String display) {
    int results = 0;
    for (ProgressStatusEnum enums : ProgressStatusEnum.values()) {
      if (enums.getDisplay().equals(display)) {
        return enums.getStatus();
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
