package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.exception.ConflictException;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;


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
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();

    }

  }

  @Override
  public UserDTO register(UserDTO userDTO) {
    try {
      dal.startTransaction();
      if (dataService.getOne(userDTO.getEmail()) != null) {
        throw new ConflictException("This email already exist");
      }
      User user = (User) userDTO;

      user.setPassword(user.hashPassword(user.getPassword()));

      UserDTO userDATA = dataService.createOne(user);
      return userDATA;
    } catch (FatalException e) {
      dal.rollBackTransaction();

      throw e;
    } finally {

      dal.commitTransaction();


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
        userToChange.setVersion(userToChange.getVersion() + 1);
        dal.commitTransaction();
        return userToChange;
      }
      return null;
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    }

  }

  public UserDTO makeManager(UserDTO userToChange) {
    try {
      dal.startTransaction();
      User user = (User) userToChange;
      if (user.changeToManager()) {
        dataService.update(userToChange);
        userToChange.setVersion(userToChange.getVersion() + 1);
        dal.commitTransaction();
        return userToChange;
      }
      return null;
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    }

  }

  @Override
  public String getPicture(int id) {
    try {
      dal.startTransaction();
      return dataService.getPicture(id);
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public UserDTO update(UserDTO newUser, String actualPassword) {
    try {
      UserDTO userToChange = getOne(newUser.getId());
      if (userToChange == null) {
        throw new FatalException("This user do not exists");
      }
      dal.startTransaction();
      User user = (User) userToChange;
      if (!user.checkPassword(actualPassword)) {
        throw new FatalException("Wrong password");
      }
      userToChange.setEmail(newUser.getEmail());
      userToChange.setGsm(newUser.getGsm());
      userToChange.setNom(newUser.getNom());
      userToChange.setPrenom(newUser.getPrenom());
      String password = newUser.getPassword();

      if (newUser.getImage() != null) {
        userToChange.setImage(newUser.getImage());
      }
      if (password != null && !password.isEmpty() && !password.isBlank()) {
        userToChange.setPassword(user.hashPassword(password));
      }
      dataService.update(userToChange);
      userToChange.setVersion(userToChange.getVersion() + 1);
      dal.commitTransaction();
      return userToChange;
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    }
  }


}
