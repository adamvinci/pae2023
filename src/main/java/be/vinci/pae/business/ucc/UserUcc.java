package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.UserFactory;

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

   Registers a new user.

   Checks if user with given email already exists, if yes returns null, otherwise registers the user by creating a new user in the database and returns the newly created user as a UserDTO.

   @param userDTO the UserDTO object containing the user's details to be registered

   @return the UserDTO object of the newly registered user, or null if a user with the given email already exists or registration fails
   */
  UserDTO register(UserDTO userDTO);
}
