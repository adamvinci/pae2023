package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.UserDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * User Class.
 */
@JsonDeserialize(as = UserImpl.class)
public interface User extends UserDTO {

  /**
   * Verify if the password match.
   *
   * @param password is the password to compare with the user password
   * @return true if the password match, false if not
   */
  boolean checkPassword(String password);

  /**
   * Hash the password of the user.
   *
   * @param password to hash
   * @return the hashed password
   */
  String hashPassword(String password);

  /**
   * Check if the user can gain the role of 'aidant'.
   *
   * @return true if the user  is not already an 'aidant' or 'responsable', false if not
   */
  boolean checkCanBeAdmin();

  /**
   * Change the role of a member into a 'aidant'.
   *
   * @return true if the user is now an 'aidant',false if not
   */
  boolean changeToAdmin();


}
