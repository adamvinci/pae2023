package be.vinci.pae.api;

import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.domain.UserUcc;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/auths")
public class UserRessource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  UserUcc userUcc;

  /**
   * Login by providing an email and a password.
   *
   * @param json contains the email and password
   * @return a json object with a token(formed by the user id) the user id and the user email
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {
    if (!json.hasNonNull("email") || !json.hasNonNull("password")) {
      throw new WebApplicationException("email or password required", Status.BAD_REQUEST);
    }

    String email = json.get("email").asText();
    String password = json.get("password").asText();

    UserDTO userDTO = userUcc.login(email, password);
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("user", userDTO.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", userDTO.getId())
          .put("email", userDTO.getEmail());
      return publicUser;

    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }

  }

}
