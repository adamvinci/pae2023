package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.dal.UserDataService;
import jakarta.inject.Inject;


/**
 * Implementation of UserUCC.
 */
public class UserUccImpl implements UserUcc {

    @Inject
    private UserDataService dataService;

    @Override
    public UserDTO login(String email, String password) {

        UserDTO userDTO = dataService.getOne(email);

        if (userDTO == null) {
            return null;
        }

        User user = (User) userDTO;
        if (!user.checkPassword(password)) {
            return null;
        }

        return userDTO;
    }

    @Override
    public UserDTO getOne(int id) {
        return dataService.getOne(id);
    }
}
