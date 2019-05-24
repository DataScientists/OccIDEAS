package org.occideas.exceptions;

public class GenericException extends Throwable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private String message;
  private Throwable e;

  public GenericException(String message, Throwable e) {
    this.message = message;
    this.e = e;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Throwable getE() {
    return e;
  }

  public void setE(Throwable e) {
    this.e = e;
  }


}
