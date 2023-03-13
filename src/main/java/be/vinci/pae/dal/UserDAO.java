package be.vinci.pae.dal;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.dto.UserDTO;

/**
 * UserDataService purpose is to  communicate with the database.
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

   This method creates a new user and stores their information in the database.
   It takes in a UserDTO object representing the user to be created and returns
   a UserDTO object representing the created user with their information updated.
   @param user a UserDTO object representing the user to be created.
   @return a UserDTO object representing the created user with their information updated.
   If the user could not be created, it returns null.
   @throws RuntimeException if there is an error while interacting with the database.
   */
  UserDTO createOne(UserDTO user);
}
