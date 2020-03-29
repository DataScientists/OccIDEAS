package org.occideas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NodeNotFoundException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private String message;

  public NodeNotFoundException(long idNode) {
    this.setMessage("Node does not exist "+idNode);
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
