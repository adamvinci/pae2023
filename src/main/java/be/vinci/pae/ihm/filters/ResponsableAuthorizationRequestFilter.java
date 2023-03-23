package be.vinci.pae.ihm.filters;


import be.vinci.pae.business.dto.UserDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;


/**
 * ResponsableAuthorizationRequestFilter treat the token send by the  method with the
 * ResponsableAuthorization Annotation.
 */
@Singleton
@Provider
@ResponsableAuthorization
public class ResponsableAuthorizationRequestFilter implements ContainerRequestFilter {

  @Inject
  private TokenFilter tokenFilter;

  /**
   * If the token is valid and the user associated with it is authorized to access the requested
   * resource.
   *
   * @param requestContext the context object representing the incoming request
   */
  @Override
  public void filter(ContainerRequestContext requestContext) {

    UserDTO authenticatedUser = tokenFilter.tokenFilter(requestContext);

    if (!authenticatedUser.getRole().equals("responsable")) {
      throw new WebApplicationException("You do not have the required permissions to do this.",
          Status.FORBIDDEN);
    }

    requestContext.setProperty("user", authenticatedUser);

  }

}