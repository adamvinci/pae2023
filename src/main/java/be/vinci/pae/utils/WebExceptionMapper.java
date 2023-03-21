package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WebExceptionMapper manage the exception send by the api.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    if (exception instanceof FatalException) {
      Logger.getLogger(MyLogger.class.getName())
          .log(Level.INFO, exception.getCause().getMessage());
      // Dont return the message in the Response as client doesnt
      // need to know the problem with the database
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(exception.getCause()).build();
    }

    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, exception.getMessage());
    if (exception instanceof WebApplicationException) {
      return Response.status(((WebApplicationException) exception).getResponse().getStatus())
          .entity(exception.getMessage())
          .build();

    }

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())

        .build();
  }
}