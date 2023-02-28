package be.vinci.pae.domain;

import be.vinci.pae.services.UserDataService;
import jakarta.inject.Inject;


/**
 * UserUccImpl.
 */
public class UserUccImpl implements UserUcc {

  @Inject
  private UserDataService dataService;

  @Override
  public UserDTO login(String email, String password) {

    UserDTO userDTO = dataService.getOne(email);

    if (userDTO == null) {
      return  null;
    }

    User user = (User) userDTO;
    if (!user.checkPassword(password)) {
      return null;
    }

    return userDTO;
  }

  @Override
  public UserDTO getOne(int id) {
    return dataService.getOne(id);
  }
}
