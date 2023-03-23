package be.vinci.pae.ihm.filters;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

/**
 * AuthorizationRequestFilter treat the token send by the  method with the Authorize Annotation.
 */
@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  @Inject
  private TokenFilter tokenFilter;

  /**
   * Verifiy that the content of the token matches the signature.
   *
   * @param requestContext contains the token
   */
  @Override
  public void filter(ContainerRequestContext requestContext) {

      UserDTO authenticatedUser = tokenFilter.tokenFilter(requestContext);
      if (authenticatedUser == null) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("You are forbidden to access this resource").build());
      }

      requestContext.setProperty("user", authenticatedUser);
    }
  }



