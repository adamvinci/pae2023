package be.vinci.pae.ihm;

import be.vinci.pae.business.ucc.UserUcc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
  public ObjectNode getAllUsers() {
    return null;
  }
}
