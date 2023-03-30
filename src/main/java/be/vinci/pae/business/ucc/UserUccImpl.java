package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.ConflictException;
import be.vinci.pae.utils.FatalException;
import jakarta.inject.Inject;
import java.util.List;


/**
 * Implementation of UserUCC.
 */
public class UserUccImpl implements UserUcc {


  @Inject
  private DALTransaction dal;
  @Inject
  private UserDAO dataService;

  @Override
  public UserDTO login(String email, String password) {
    Exception e1 = null;
    try {
      dal.startTransaction();
      UserDTO userDTO = dataService.getOne(email);

      if (userDTO == null) {
        return null;
      }

      User user = (User) userDTO;
      if (!user.checkPassword(password)) {
        return null;
      }

      return userDTO;
    } catch (Exception e) {

      try {
        dal.rollBackTransaction();
      } catch (Exception rollbackException) {
        e.addSuppressed(rollbackException);
      }
      e1 = e;
      throw e;
    } finally {
      try {
        dal.commitTransaction();
      } catch (Exception commitException) {
        if (e1 != null) {
          e1.addSuppressed(commitException);
        } else {
          throw commitException;
        }
      }
    }

  }

  @Override
  public UserDTO register(UserDTO userDTO) {
    Exception e1 = null;
    try {
      dal.startTransaction();
      if (dataService.getOne(userDTO.getEmail()) != null) {
        throw new ConflictException("This email already exist");
      }
      User user = (User) userDTO;

      user.setPassword(user.hashPassword(user.getPassword()));

      UserDTO userDATA = dataService.createOne(user);
      return userDATA;
    } catch (Exception e) {

      try {
        dal.rollBackTransaction();
      } catch (Exception rollbackException) {
        e.addSuppressed(rollbackException);
      }
      e1 = e;
      throw e;
    } finally {
      try {
        dal.commitTransaction();
      } catch (Exception commitException) {
        if (e1 != null) {
          e1.addSuppressed(commitException);
        } else {
          throw commitException;
        }
      }
    }
  }

  @Override
  public UserDTO getOne(int id) {
    try {
      dal.startTransaction();
      return dataService.getOne(id);
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public List<UserDTO> getAll() {
    try {
      dal.startTransaction();
      return dataService.getAll();
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public UserDTO makeAdmin(UserDTO userToChange) {
    try {
      dal.startTransaction();
      User user = (User) userToChange;
      if (user.changeToAdmin()) {
        dataService.update(userToChange);
        return userToChange;
      }
      return null;
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }

  }
}
