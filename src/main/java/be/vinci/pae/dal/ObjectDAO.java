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

  /**
   * Retrieve the path of the image of the object in the DB.
   *
   * @param id linked to an object
   * @return the path of the picture or null
   */
  String getPicture(int id);

  /**
   * Modifies the state of an object to either "accepted" or "rejected".
   *
   * @param etat     The new state of the object, either "accepted" or "rejected".
   * @param id_objet The object will be modified.
   * @return Returns true if the state was successfully modified, false otherwise.
   */
  boolean modifierEtatObjet(String etat, int id_objet);
}
