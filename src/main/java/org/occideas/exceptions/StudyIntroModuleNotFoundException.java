package org.occideas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudyIntroModuleNotFoundException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private String message;

  public StudyIntroModuleNotFoundException() {
    this.setMessage("Set intro module in admin function.");
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
