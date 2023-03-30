package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class ConflictException extends WebApplicationException {

  public ConflictException(String message) {
    super(message, Status.CONFLICT);
  }
}
