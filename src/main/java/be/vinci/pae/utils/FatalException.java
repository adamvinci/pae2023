package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;

/**
 * A custom exception class that represents a fatal error in a web application.
 */
public class FatalException extends WebApplicationException {

  /**
   * Constructs a new FatalException object with the specified cause.
   *
   * @param cause the Throwable object that caused the exception
   */
  public FatalException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new FatalException object with the specified cause.
   *
   * @param m the message
   */
  public FatalException(String m) {
    super(m);
  }
}
