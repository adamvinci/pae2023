package be.vinci.pae.ihm;


import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.UserFactory;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.ihm.filters.PictureService;
import be.vinci.pae.ihm.filters.ResponsableAuthorization;
import be.vinci.pae.ihm.filters.ResponsableOrAidant;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Json;
import be.vinci.pae.utils.MyLogger;
import be.vinci.pae.utils.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * UserRessource retrieve the request  process by Grizzly and treat it.
 */
@Singleton
@Path("/users")
public class UserRessource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private UserUcc userUcc;

  @Inject
  private UserFactory userFactory;
  @Inject
  private PictureService pictureService;


  /**
   * Get all the users.
   *
   * @return a json object with all the users
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ResponsableAuthorization
  public List<UserDTO> getAllUsers() {
    List<UserDTO> users = userUcc.getAll();
    for (int index = 0; index < users.size(); index++) {
      UserDTO nonFilteredUser = users.get(index);
      users.set(index, Json.filterPublicJsonView(nonFilteredUser, UserDTO.class));
    }
    return users;
  }

  /**
   * Get the corresponding users.
   *
   * @param id to search
   * @return the user
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @ResponsableOrAidant
  public UserDTO getOneUser(@PathParam("id") int id) {
    if (id == -1) {
      throw new WebApplicationException("Id of photo required", Status.BAD_REQUEST);
    }
    UserDTO userDTO = userUcc.getOne(id);
    if (userDTO == null) {
      throw new WebApplicationException("No user for this id", Status.NOT_FOUND);
    }
    return Json.filterPublicJsonView(userDTO, UserDTO.class);
  }

  /**
   * Change the role of a user to make him "aidant".
   *
   * @param id of the user
   * @return a json object with the modified user.
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}/confirmHelper")
  @ResponsableAuthorization
  public UserDTO confirmHelper(
      @DefaultValue("-1") @PathParam("id") int id) {
    if (id == -1) {
      throw new WebApplicationException("No content", Status.BAD_REQUEST);
    }

    UserDTO userToChange = userUcc.getOne(id);
    if (userToChange == null) {
      throw new WebApplicationException("This user does not exist", Status.BAD_REQUEST);
    }
    UserDTO changedUser = userUcc.makeAdmin(userToChange);
    if (changedUser == null) {
      throw new WebApplicationException(
          "This user can't become an 'aidant' because "
              + "he already has the role 'aidant' or 'responsable'",
          412);
    }
    return changedUser;
  }


  /**
   * Change the role of a user to make him "responsable".
   *
   * @param id of the user
   * @return a json object with the modified user.
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}/confirmManager")
  @ResponsableAuthorization
  public UserDTO confirmManager(
      @DefaultValue("-1") @PathParam("id") int id) {
    if (id == -1) {
      throw new WebApplicationException("No content", Status.BAD_REQUEST);
    }

    UserDTO userToChange = userUcc.getOne(id);
    if (userToChange == null) {
      throw new WebApplicationException("This user does not exist", Status.BAD_REQUEST);
    }
    UserDTO changedUser = userUcc.makeManager(userToChange);
    if (changedUser == null) {
      throw new WebApplicationException(
          "This user can't become an 'responsable' because "
              + "he already has the role 'responsable'",
          412);
    }
    return changedUser;
  }


  /**
   * Update the informations of an user.
   *
   * @param id           of the user
   * @param newUsersData the new informations that the user typed in.
   * @return a json object with the modified user.
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}")
  @Authorize
  public UserDTO update(JsonNode newUsersData,
      @DefaultValue("-1") @PathParam("id") int id) {
    if (id == -1) {
      throw new WebApplicationException("No content", Status.BAD_REQUEST);
    }

    if (!newUsersData.hasNonNull("nom") || !newUsersData.hasNonNull("prenom")
        || !newUsersData.hasNonNull("email") || !newUsersData.hasNonNull("gsm")) {
      throw new WebApplicationException("Last name, first name, email and gsm required",
          Status.BAD_REQUEST);
    }

    String email = newUsersData.get("email").asText();
    String name = newUsersData.get("nom").asText();
    String firstName = newUsersData.get("prenom").asText();
    String gsm = newUsersData.get("gsm").asText();
    String password = newUsersData.get("password").asText();
    String confirmPassword = newUsersData.get("confirmPassword").asText();
    String actualPassword = newUsersData.get("actualPassword").asText();

    if (email.isBlank() || email.isEmpty() || name.isBlank() || name.isEmpty()
        || firstName.isBlank() || firstName.isEmpty() || gsm.isBlank() || gsm.isEmpty()) {
      throw new WebApplicationException("Last name, first name, email and gsm required",
          Status.BAD_REQUEST);
    }

    if (!password.equals("") && (password.isBlank() || password.isEmpty())) {
      throw new WebApplicationException("Can't replace your password by a blank/empty password",
          Status.BAD_REQUEST);
    }

    if (password.isBlank() != confirmPassword.isBlank()) {
      throw new WebApplicationException(
          "Don't forget to confirm your password !",
          Status.BAD_REQUEST);
    }

    if (!password.equals(confirmPassword)) {
      throw new WebApplicationException("The new password and its confirmation are not the same.",
          Status.BAD_REQUEST);
    }

    UserDTO newUser = userFactory.getUserDTO();
    newUser.setEmail(email);
    newUser.setPrenom(firstName);
    newUser.setNom(name);
    newUser.setGsm(gsm);
    newUser.setId(id);
    newUser.setPassword(password);

    UserDTO userToChange = userUcc.getOne(id);
    if (userToChange == null) {
      throw new WebApplicationException("This user does not exist", Status.BAD_REQUEST);
    }

    if (newUsersData.hasNonNull("image")) {
      if (!Files.exists(java.nio.file.Path.of(newUsersData.get("image").asText()))) {
        throw new WebApplicationException("This image is not stored in the server ",
            Status.BAD_REQUEST);
      }
      File oldAvatar = new File(userToChange.getImage());
      String[] parts = oldAvatar.toString().split("\\\\");
      String fileName = parts[parts.length - 1];
      if (!fileName.equals("avatar1.png") && !fileName.equals("avatar2.png")
          && oldAvatar.delete()) {
        Logger.getLogger(MyLogger.class.getName())
            .log(Level.INFO, "Deleted picture " + oldAvatar);
      }
      newUser.setImage(newUsersData.get("image").asText());
    }
    UserDTO changedUser;
    try {
      changedUser = userUcc.update(newUser, actualPassword);
    } catch (BusinessException e) {
      throw new WebApplicationException(e.getMessage(), Status.UNAUTHORIZED);
    }
    return changedUser;
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

