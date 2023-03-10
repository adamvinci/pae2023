package be.vinci.pae.ihm.filters;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * TokenDecodingException manage the exception  launched by the treatment of the token.
 */
public class TokenDecodingException extends WebApplicationException {

  /**
   * Creates a new TokenDecodingException with UNAUTHORIZED status.
   */
  public TokenDecodingException() {
    super(Response.Status.UNAUTHORIZED);
  }

  /**
   * Creates a new TokenDecodingException with the specified message and UNAUTHORIZED status.
   *
   * @param message the error message
   */
  public TokenDecodingException(String message) {
    super(message, Response.Status.UNAUTHORIZED);
  }

  /**
   * Creates a new TokenDecodingException with the specified cause and UNAUTHORIZED status.
   *
   * @param cause the Throwable that caused this exception
   */
  public TokenDecodingException(Throwable cause) {
    super(cause.getMessage(), Response.Status.UNAUTHORIZED);
  }
}
