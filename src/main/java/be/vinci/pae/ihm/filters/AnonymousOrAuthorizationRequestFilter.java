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
 * This filter allows anonymous requests.
 */
@Singleton
@Provider
@AnonymousOrAuthorize
public class AnonymousOrAuthorizationRequestFilter implements ContainerRequestFilter {

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
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      return;
    }
    UserDTO authenticatedUser = tokenFilter.tokenFilter(requestContext);
    if (authenticatedUser == null) {
      throw new WebApplicationException("You are forbidden to access this resource",
          Status.FORBIDDEN);
    }

    requestContext.setProperty("user", authenticatedUser);

  }

}