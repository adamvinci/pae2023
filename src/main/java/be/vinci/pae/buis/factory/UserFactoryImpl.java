package be.vinci.pae.buis.factory;

import be.vinci.pae.buis.biz.UserImpl;
import be.vinci.pae.buis.dto.UserDTO;

/**
 * Implementation of UserFactory.
 */
public class UserFactoryImpl implements UserFactory {

  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }

}
