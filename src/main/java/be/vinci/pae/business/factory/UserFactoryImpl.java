package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.UserImpl;
import be.vinci.pae.business.dto.UserDTO;

/**
 * Implementation of UserFactory.
 */
public class UserFactoryImpl implements UserFactory {

    @Override
    public UserDTO getUserDTO() {
        return new UserImpl();
    }

}
