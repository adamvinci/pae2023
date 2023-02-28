package be.vinci.pae.domain;

/**
 * Implementation of UserFactory.
 */
public class UserFactoryImpl implements UserFactory {

  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }

}
