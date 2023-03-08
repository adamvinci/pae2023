package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import java.util.List;

/**
 * ObjetUCC acts as an orchestrator to allow {@link be.vinci.pae.ihm.ObjetRessource} and
 * {@link be.vinci.pae.dal.ObjectDAO} layers to communicate.
 */
public interface ObjetUCC {

  /**
   * Retrieve all the object in the database.
   *
   * @return a list of object
   */
  List<ObjetDTO> getAllObject();

  /**
   * Retrieve all the type of object in the database.
   *
   * @return a list of object type
   */
  List<TypeObjetDTO> getAllObjectType();
}
