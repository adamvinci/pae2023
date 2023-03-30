package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class BusinessException extends WebApplicationException {

  public BusinessException(String message) {
    super(message, Status.PRECONDITION_FAILED);
  }
}
