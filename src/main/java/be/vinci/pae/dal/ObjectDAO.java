package be.vinci.pae.dal;

import be.vinci.pae.business.dto.ObjetDTO;
import java.util.List;

/**
 * ObjectDAO purpose is to communicate with the database.
 */
public interface ObjectDAO {

  List<ObjetDTO> getAllObjet();
}
