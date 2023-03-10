package be.vinci.pae.business.factory;

import be.vinci.pae.business.dto.UserDTO;

/**
 * UserFactory provide an instance of {@link UserDTO}.
 */
public interface UserFactory {

  UserDTO getUserDTO();
}
