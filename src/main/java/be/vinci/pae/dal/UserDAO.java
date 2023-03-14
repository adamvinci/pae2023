package be.vinci.pae.dal;

import be.vinci.pae.business.dto.UserDTO;
import java.util.List;

/**
 * UserDAO purpose is to communicate with the database.
 */
public interface UserDAO {

  /**
   * Ask the database if the email match with an existing user.
   *
   * @param email to check
   * @return the matching user or null
   */
  UserDTO getOne(String email);

  /**
   * Ask the database if the id match with the id of an existing user.
   *
   * @param id to check
   * @return the matching user or null
   */
  UserDTO getOne(int id);


  /**
   * This method creates a new user and stores their information in the database.
   *
   * @param user a UserDTO object representing the user to be created.
   * @return a UserDTO object representing the created user with their information updated or null
   */
  UserDTO createOne(UserDTO user);


  /**
   * Ask the database to get all the users.
   *
   * @return users
   */
  List<UserDTO> getAll();
}
