package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.UserImpl;
import be.vinci.pae.business.dto.UserDTO;

/**
 * Implementation  of {@link UserFactory}.
 */
public class UserFactoryImpl implements UserFactory {

  /**
   * Returns a new {@link  UserDTO} object.
   *
   * @return a new {@link UserDTO} object
   */
  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }

}
