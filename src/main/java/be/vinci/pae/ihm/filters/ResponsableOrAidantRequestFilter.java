package be.vinci.pae.ihm.filters;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.ihm.filters.TokenService;
import be.vinci.pae.ihm.filters.ResponsableOrAidant;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

/**
 * Verify that the token belong to an 'aidant' or to the 'Responsable'.
 */
@Singleton
@Provider
@ResponsableOrAidant
public class ResponsableOrAidantRequestFilter implements
    ContainerRequestFilter {

  @Inject
  private TokenService tokenService;

  /**
   * If the token is valid and the user associated with it is authorized to access the requested
   * resource.
   *
   * @param requestContext the context object representing the incoming request
   */
  @Override
  public void filter(ContainerRequestContext requestContext) {

    UserDTO authenticatedUser = tokenService.tokenFilter(requestContext);
    if (!authenticatedUser.getRole().equals("responsable") && !authenticatedUser.getRole()
        .equals("aidant")) {
      throw new WebApplicationException("You do not have the required permissions to do this.",
          Status.FORBIDDEN);
    }

    requestContext.setProperty("user", authenticatedUser);

  }


}
