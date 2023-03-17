package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;

public class FatalException extends WebApplicationException {

  public FatalException(Throwable cause) {
    super(cause);
  }
}
