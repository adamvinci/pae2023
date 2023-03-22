package be.vinci.pae.ihm;


import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.ihm.filters.ResponsableAuthorization;
import be.vinci.pae.utils.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

/**
 * UserRessource retrieve the request  process by Grizzly and treat it.
 */
@Singleton
@Path("/users")
public class UserRessource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private UserUcc userUcc;


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
   * Change the role of a user to make him "aidant".
   *
   * @param id of the object
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
      throw new WebApplicationException("This user can't become an 'aidant' because he already has the role 'aidant' or 'responsable'", Status.BAD_REQUEST);
    }
    return changedUser;
  }
}

