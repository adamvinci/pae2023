package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

/**
 * UserUcc acts  as an orchestrator to allow the IHM and DAL layers to communicate.
 */
public interface UserUcc {

  /**
   * Check if the email and password provided match with an existing user in the DataBase.
   *
   * @param email    of the user
   * @param password of the user
   * @return the matching user or an exception
   */
  UserDTO login(String email, String password);

  /**
   * Check if the id of the token with the id of an existing user in the DataBase.
   *
   * @param id of the user
   * @return the matching user or an exception
   */
  UserDTO getOne(int id);


  /**
   * Get the list of all the users of the database.
   *
   * @return the List with every users
   */
  List<UserDTO> getAll();
}
