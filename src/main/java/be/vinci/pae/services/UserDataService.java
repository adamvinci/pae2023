package be.vinci.pae.services;

import be.vinci.pae.domain.UserDTO;

public interface UserDataService {

  UserDTO getOne(String email);
}
