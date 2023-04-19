package be.vinci.pae.ihm;


import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.UserFactory;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.ihm.filters.ResponsableAuthorization;
import be.vinci.pae.ihm.filters.ResponsableOrAidant;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Json;
import be.vinci.pae.utils.MyLogger;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    UserDTO userToChange = userUcc.getOne(id);

    UserDTO newUser = userFactory.getUserDTO();
    newUser.setEmail(email);
    newUser.setPrenom(firstName);
    newUser.setNom(name);
    newUser.setGsm(gsm);
    newUser.setId(id);
    newUser.setPassword(password);

    if (newUsersData.hasNonNull("image")) {
      File oldAvatar = new File(userToChange.getImage());
      String[] parts = oldAvatar.toString().split("\\\\");
      String fileName = parts[parts.length - 1];
      if(!fileName.equals("avatar1.png") && !fileName.equals("avatar2.png")){
        if(oldAvatar.delete()){
          Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Deleted picture "+oldAvatar);
        }

      }
      newUser.setImage(
          Config.getProperty("pathToUserImage") + newUsersData.get("image").asText());
    }
    UserDTO changedUser = userUcc.update(newUser);
    return changedUser;
  }
}

