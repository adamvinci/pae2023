package be.vinci.pae.dal;

import be.vinci.pae.business.dto.TypeObjetDTO;
import java.util.List;

/**
 * TypeObjetDAO purpose is to communicate with the database.
 */
public interface TypeObjetDAO {

  /**
   * Retrieve the type of object corresponding the id.
   *
   * @param id to search
   * @return the Type or else null
   */
  TypeObjetDTO getOne(int id);

  /**
   * Retrieve the type of object in the database.
   *
   * @return a list of object type or null if there are none.
   */
  List<TypeObjetDTO> getAll();
}
