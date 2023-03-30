package be.vinci.pae.utils.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Constructs a new 409 object with the specified cause.
 */
public class ConflictException extends WebApplicationException {

  /**
   * Constructs a new ConflictException object with the specified cause.
   *
   * @param message the message
   */
  public ConflictException(String message) {
    super(message, Status.CONFLICT);
  }
}
