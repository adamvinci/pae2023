package be.vinci.pae.utils.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * A custom exception class that represents a 412 error in a web application.
 */
public class BusinessException extends WebApplicationException {

  /**
   * Constructs a new BusinessException object with the specified cause.
   *
   * @param message the message
   */
  public BusinessException(String message) {
    super(message, Status.PRECONDITION_FAILED);
  }
}
