package be.vinci.pae.dal;

import be.vinci.pae.business.dto.ObjetDTO;
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


}
