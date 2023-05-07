package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.TypeObjetDTO;
import java.util.List;

/**
 * ObjetUCC acts as an orchestrator to allow {@link be.vinci.pae.ihm.TypeObjetRessource} and
 *  * {@link be.vinci.pae.dal.TypeObjetDAO} layers to communicate.
 */
public interface TypeObjetUcc {

  /**
   * Retrieve all the type of object in the database.
   *
   * @return a list of object type
   */
  List<TypeObjetDTO> getAllObjectType();

  /**
   * Retrieves a TypeObjetDTO object corresponding to the specified ID.
   *
   * @param id the ID of the TypeObjetDTO to retrieve.
   * @return the TypeObjetDTO object with the specified ID.
   */
  TypeObjetDTO getOneType(int id);

}
