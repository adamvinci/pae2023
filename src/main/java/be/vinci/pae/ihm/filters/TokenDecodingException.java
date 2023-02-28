package be.vinci.pae.ihm.filters;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * TokenDecodingException manage the exception launched by the treatment of the token.
 */
public class TokenDecodingException extends WebApplicationException {

  public TokenDecodingException() {
    super(Response.Status.UNAUTHORIZED);
  }

  public TokenDecodingException(String message) {
    super(message, Response.Status.UNAUTHORIZED);
  }

  public TokenDecodingException(Throwable cause) {
    super(cause.getMessage(), Response.Status.UNAUTHORIZED);
  }
}