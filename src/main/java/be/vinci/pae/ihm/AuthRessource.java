package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Json;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * UserRessource retrieve the request  process by Grizzly and treat it.
 */
@Singleton
@Path("/auths")
public class UserRessource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private UserUcc userUcc;

  /**
   * Login by providing an email and a password.
   *
   * @param userCredentials contains the email and password
   * @return a json object with a token(formed by the user id) the user id and the user email
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode userCredentials) {
    if (!userCredentials.hasNonNull("email") || !userCredentials.hasNonNull("password")) {
      throw new WebApplicationException("email or password required", Status.BAD_REQUEST);
    }

    String email = userCredentials.get("email").asText();
    String password = userCredentials.get("password").asText();

    UserDTO userDTO = userUcc.login(email, password);

    if (userDTO == null) {
      throw new WebApplicationException("bad credentials", Status.BAD_REQUEST);
    }

    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", userDTO.getId()).sign(this.jwtAlgorithm);

      return jsonMapper.createObjectNode()
          .put("token", token)
          .putPOJO("user", Json.filterPublicJsonView(userDTO, UserDTO.class));

    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }

  }

  /**
   * Path to retrieve the user from a token.
   *
   * @param request contains the token in the header
   * @return the user contained in the token
   */
  @GET
  @Path("user")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getUser(@Context ContainerRequest request) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    return jsonMapper.createObjectNode()
        .putPOJO("User", Json.filterPublicJsonView(userDTO, UserDTO.class));

  }

}
