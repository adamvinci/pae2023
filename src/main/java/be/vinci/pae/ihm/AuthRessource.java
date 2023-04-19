package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.ihm.filters.PictureService;
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
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ContainerRequest;

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
  @Inject
  private PictureService pictureService;


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
      throw new WebApplicationException("this image is not saved ", Status.BAD_REQUEST);
    }
    userDTO = userUcc.register(userDTO);

    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Inscription de "
        + userDTO.getEmail());
    return Json.filterPublicJsonView(userDTO, UserDTO.class);
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
  public UserDTO getUser(@Context ContainerRequest request) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Retrieve user from token of "
        + userDTO.getEmail());
    return Json.filterPublicJsonView(userDTO, UserDTO.class);

  }

  /**
   * Upload the avatar image of a user.
   *
   * @param file            the image
   * @param fileDisposition the file data as a FormDataContentDisposition object, containing the
   *                        file name and metadata
   * @return a Response object indicating success or failure of the file upload
   */
  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_PLAIN)
  public String uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) {
    String fileName = fileDisposition.getFileName();
    String pathToSave = Config.getProperty("pathToUserImage");
    String newFileName = UUID.randomUUID() + "." + fileName;
    pathToSave += newFileName;

    try {
      Files.copy(file, Paths.get(pathToSave));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Adding avatar");
    return pathToSave;
  }

  /**
   * Retrieve the picture of a user.
   *
   * @param id      of the user
   * @param request contains the user asking the picture
   * @return the image
   */
  @Authorize
  @GET
  @Path("getPicture/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({"image/png", "image/jpg", "image/jpeg"})
  public Response getPictureUser(@DefaultValue("-1") @PathParam("id") int id,
      @Context ContainerRequest request) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    if (id == -1) {
      throw new WebApplicationException("Id of photo required", Status.BAD_REQUEST);
    }
    if (userDTO.getId() != id && userDTO.getRole().equals("membre")) {
      throw new WebApplicationException("You cant acces avatar from other user",
          Status.UNAUTHORIZED);
    }
    String pathPicture = userUcc.getPicture(id);
    if (pathPicture == null) {
      throw new WebApplicationException("No image for this user in the database",
          Status.NOT_FOUND);
      // delete from img if exists
    }

    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Retrieve picture of user " + id);
    return pictureService.transformImage(pathPicture);
  }


}
