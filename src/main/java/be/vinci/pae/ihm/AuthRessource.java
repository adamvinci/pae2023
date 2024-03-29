package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Json;
import be.vinci.pae.utils.MyLogger;
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
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AuthRessource retrieve the request process by Grizzly and treat it.
 */
@Singleton
@Path("/auths")
public class AuthRessource {

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
      throw new WebApplicationException("Email or password required", Status.BAD_REQUEST);
    }

    String email = userCredentials.get("email").asText();
    String password = userCredentials.get("password").asText();

    if (email.isBlank() || email.isEmpty() || password.isBlank() || password.isEmpty()) {
      throw new WebApplicationException("Email or password required", Status.BAD_REQUEST);
    }

    UserDTO userDTO = userUcc.login(email, password);
    if (userDTO == null) {
      throw new WebApplicationException("Bad credentials", Status.UNAUTHORIZED);
    }

    String token;
    try {
      token = JWT.create().withExpiresAt(new Date(System.currentTimeMillis() + (86400 * 1000)))
          .withIssuer("auth0")
          .withClaim("user", userDTO.getId()).sign(this.jwtAlgorithm);

    } catch (Exception e) {
      throw new WebApplicationException(e.getMessage(), Status.FORBIDDEN);
    }
    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Connexion de " + email);
    return jsonMapper.createObjectNode()
        .put("token", token)
        .putPOJO("user", Json.filterPublicJsonView(userDTO, UserDTO.class));

  }


  /**
   * This method registers a new user using the provided userDTO object.
   *
   * @param userDTO the userDTO object containing the user's information.
   * @return an ObjectNode containing the user's JWT token and public information.
   */
  @POST
  @Path("register")
  @Consumes({MediaType.APPLICATION_JSON})
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO register(UserDTO userDTO) {
    if (userDTO.getEmail().equals("") || userDTO.getEmail().isBlank()
        || userDTO.getPassword().equals("") || userDTO.getPassword().isBlank()
        || userDTO.getNom().equals("") || userDTO.getNom().isBlank()
        || userDTO.getPrenom().equals("") || userDTO.getPrenom().isBlank()
        || userDTO.getGsm().equals("") || userDTO.getGsm().isBlank() || userDTO.getImage().isBlank()
        || userDTO.getImage().equals("")) {
      throw new WebApplicationException("missing fields", Status.BAD_REQUEST);
    }
    if (!Files.exists(java.nio.file.Path.of(userDTO.getImage()))) {
      throw new WebApplicationException("this image is not stored in the server ",
          Status.BAD_REQUEST);
    }
    userDTO = userUcc.register(userDTO);

    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Inscription de "
        + userDTO.getEmail());
    return Json.filterPublicJsonView(userDTO, UserDTO.class);
  }



}
