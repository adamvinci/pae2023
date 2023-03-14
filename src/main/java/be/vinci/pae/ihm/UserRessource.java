package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.utils.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
   * @return a json object with a token(formed by the user id) the user id and the user email
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> getAllUsers() {
    List<UserDTO> users = userUcc.getAll();
    for (int index = 0; index < users.size(); index++) {
      UserDTO nonFilteredUser = users.get(index);
      users.set(index, Json.filterPublicJsonView(nonFilteredUser, UserDTO.class));
    }
    return users;
  }
}
