package be.vinci.pae.domain;

import be.vinci.pae.services.UserDataService;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * UserUccImpl.
 */
public class UserUccImpl implements UserUcc {

  @Inject
  private UserDataService dataService;

  @Override
  public UserDTO login(String email, String password) {

    if ((email == null || email.isBlank()) && (password == null || password.isBlank())) {
      throw new WebApplicationException("Champ login ou mot de passe sont vide",
          Status.BAD_REQUEST);
    }

    if ((email == null || email.isBlank()) && !password.isBlank()) {
      throw new WebApplicationException("Completer le champ username", Status.BAD_REQUEST);
    }

    if (!email.isBlank() && (password == null || password.isBlank())) {
      throw new WebApplicationException("Completer le chanp password", Status.BAD_REQUEST);
    }

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
