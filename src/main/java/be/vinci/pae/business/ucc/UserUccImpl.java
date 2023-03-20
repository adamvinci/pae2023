package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.domaine.UserImpl;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.dal.UserDAO;
import jakarta.inject.Inject;
import java.util.List;


/**
 * Implementation of UserUCC.
 */
public class UserUccImpl implements UserUcc {

  @Inject
  private UserDAO dataService;

  @Override
  public UserDTO login(String email, String password) {

    UserDTO userDTO = dataService.getOne(email);

    if (userDTO == null) {
      return null;
    }

    User user = (User) userDTO;
    if (!user.checkPassword(password)) {
      return null;
    }

    return userDTO;
  }

  @Override
  public UserDTO register(UserDTO userDTO) {
    if (dataService.getOne(userDTO.getEmail()) != null) {
      return null;
    }
    User user = (User) userDTO;

    user.setPassword(user.hashPassword(user.getPassword()));

    UserDTO userDATA = dataService.createOne(user);
    if (userDATA == null) {
      return null;
    }
    return userDATA;

  }

  @Override
  public UserDTO getOne(int id) {
    return dataService.getOne(id);
  }

  @Override
  public List<UserDTO> getAll() {
    return dataService.getAll();
  }

  @Override
  public UserDTO makeAdmin(UserDTO userToChange) {
    User user = (User) userToChange;
    if (user.checkCanBeAdmin()) {
      user.changeToAdmin();
      dataService.update(userToChange);
      return userToChange;
    }
    return null;

  }


}
