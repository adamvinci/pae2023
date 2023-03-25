package be.vinci.pae.ihm.filters;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response.Status;

/**
 * Implementation of {@link TokenFilter}.
 */
public class TokenFilterImpl implements TokenFilter {
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();
  @Inject
  private UserUcc userUcc;

  @Override
  public UserDTO tokenFilter(ContainerRequestContext requestContext) {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      throw new WebApplicationException("You must be connected to access this ressource",
          Status.UNAUTHORIZED);
    }

    DecodedJWT decodedToken = null;
    try {
      decodedToken = this.jwtVerifier.verify(token);
    } catch (Exception e) {
      throw new WebApplicationException("Malformed token : " + e.getMessage(), Status.UNAUTHORIZED);
    }
    UserDTO authenticatedUser = userUcc.getOne(decodedToken.getClaim("user").asInt());
    if (authenticatedUser == null) {
      throw new WebApplicationException("You are forbidden to access this resource",
          Status.FORBIDDEN);
    }
    return authenticatedUser;
  }
}
