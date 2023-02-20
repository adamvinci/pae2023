package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * UserClass.
 */
@JsonDeserialize(as = UserImpl.class)
public interface User extends UserDTO {

  boolean checkPassword(String password);

  String hashPassword(String password);

  boolean checkCanBeAdmin(User user);

  boolean changeToAdmin(User user);


}
