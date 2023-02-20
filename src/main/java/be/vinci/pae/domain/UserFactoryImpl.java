package be.vinci.pae.domain;

/**
 * UserFactoryImpl.
 */
public class UserFactoryImpl implements UserFactory {

  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }

}
