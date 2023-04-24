package be.vinci.pae.dal;

import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import java.util.List;

/**
 * ObjectDAO purpose is to communicate with the object table in the database.
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
   * Modifies the state of an object.
   *
   * @param objetDTO to change
   * @return the changed object
   */
  ObjetDTO updateObjectState(ObjetDTO objetDTO);

  /**
   * Retrieve an object.
   *
   * @param id of object to search
   * @return the objectDTO
   */
  ObjetDTO getOne(int id);

  /**
   * Inserts a new object into the project database.
   *
   * @param objet the object to insert
   * @return the object with its generated ID, or null if the insertion failed
   */
  ObjetDTO createObjet(ObjetDTO objet);

  /**
   * Updates an object in the database with a new description, photo and/or type.
   *
   * @param objetDTO The object to be updated, containing the ID of the object and its current
   *                 version number.
   * @return The updated object.
   */
  ObjetDTO updateObject(ObjetDTO objetDTO);
}
