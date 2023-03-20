package be.vinci.pae.ihm;


import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.ihm.filters.Authorize;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
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


  /**
   * Get all the users.
   *
   * @param request request the request container
   * @return a json object with a token(formed by the user id) the user id and the user email
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<UserDTO> getAllUsers(@Context ContainerRequest request) {
    UserDTO userDTO = (UserDTO) request.getProperty("user");
    List<UserDTO> users = userUcc.getAll();
    if (checkAccountableAutorization(userDTO)) {
      for (int index = 0; index < users.size(); index++) {
        UserDTO nonFilteredUser = users.get(index);
        users.set(index, Json.filterPublicJsonView(nonFilteredUser, UserDTO.class));
      }

    } else {
      throw new WebApplicationException(
          "Only the role 'responsable' can acces to the users list",
          Status.UNAUTHORIZED);
    }
    return users;
  }

  /**
   * Change the role of an user to make him "aidant".
   *
   * @return a json object with the modified user.
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id}/confirmHelper")
  @Authorize
  public UserDTO confirmHelper(@Context ContainerRequest request,
      @DefaultValue("-1") @PathParam("id") int id) {
    if (id == -1) {
      throw new WebApplicationException("No content", Status.BAD_REQUEST);
    }

    UserDTO userDTO = (UserDTO) request.getProperty("user");
    if (checkAccountableAutorization(userDTO)) {
      UserDTO userToChange = userUcc.getOne(id);
      if (userToChange == null) {
        throw new WebApplicationException("This user does not exist", Status.BAD_REQUEST);
      }
      UserDTO changedUser = userUcc.makeAdmin(userToChange);
      if (changedUser == null) {
        throw new WebApplicationException("This user can't become an 'aidant'", Status.BAD_REQUEST);
      }
      return changedUser;
    } else {
      throw new WebApplicationException("Only the role 'responsable' can confirm an Helper",
          Status.UNAUTHORIZED);
    }
  }


  private boolean checkAccountableAutorization(UserDTO user) {
    if (user != null && user.getRole().equals("responsable")) {
      return true;
    }
    return false;
  }
}

