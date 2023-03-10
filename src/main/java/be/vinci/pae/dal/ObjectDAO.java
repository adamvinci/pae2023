package be.vinci.pae.dal;

import be.vinci.pae.business.dto.ObjetDTO;
import java.util.List;

/**
 * ObjectDAO purpose is to communicate with the database.
 */
public interface ObjectDAO {

  /**
   * Retrieve the objects in the database.
   *
   * @return a list of object or null if there are none.
   */
  List<ObjetDTO> getAllObjet();
}
