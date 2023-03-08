package be.vinci.pae.dal;

import be.vinci.pae.business.dto.TypeObjetDTO;
import java.util.List;

/**
 * TypeObjetDAO purpose is to communicate with the database.
 */
public interface TypeObjetDAO {

  TypeObjetDTO getOne(int id);

  List<TypeObjetDTO> getAll();
}
