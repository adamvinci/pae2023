package be.vinci.pae.domain;

import be.vinci.pae.services.UserDataService;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Implementation of UserUCC.
 */
public class UserUccImpl implements UserUcc {

  @Inject
  private UserDataService dataService;

  @Override
  public UserDTO login(String email, String password) {
    UserDTO userDTO = dataService.getOne(email);
    if (userDTO.getId() == null) {
      throw new WebApplicationException("Cette email n'existe pas", Response.Status.NOT_FOUND);
    }
    User user = (User) userDTO;
    if (!user.checkPassword(password)) {
      throw new WebApplicationException("Mauvais mot de passe", Status.UNAUTHORIZED);
    }
    return userDTO;
  }

  @Override
  public UserDTO getOne(int id) {
    return dataService.getOne(id);
  }
}
