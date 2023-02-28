package be.vinci.pae.dal;

import be.vinci.pae.business.dto.UserDTO;

/**
 * UserDataService purpose is to communicate with the database.
 */
public interface UserDataService {

    /**
     * Ask the database if the email match with an existing user.
     *
     * @param email to check
     * @return the matching user or null
     */
    UserDTO getOne(String email);

    /**
     * Ask the database if the id match with the id of an existing user.
     *
     * @param id to check
     * @return the matching user or null
     */
    UserDTO getOne(int id);
}
