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

  /**
   * Ask the db the image of the object.
   *
   * @param id linked to an object
   * @return the path of the picture
   */
  String getPicture(int id);

  /**

   Accepts an object represented by the given ObjetDTO object.
   @param objetDTO the object to be accepted
   @return true if the object is accepted successfully, false otherwise
   */
  ObjetDTO accepterObjet(ObjetDTO objetDTO);

  /**

   Refuses an object represented by the given ObjetDTO object.
   @param objetDTO the object to be refused
   @return true if the object is refused successfully, false otherwise
   */
  ObjetDTO refuserObject(ObjetDTO objetDTO);

  /**

   Deposits an object represented by the given ObjetDTO object.
   @param objetDTO the object to be deposited
   @return true if the object is deposited successfully, false otherwise
   */
  ObjetDTO depotObject(ObjetDTO objetDTO);

  /**

   Sells an object represented by the given ObjetDTO object.
   @param objetDTO the object to be sold
   @return true if the object is sold successfully, false otherwise
   */
  ObjetDTO venteObject(ObjetDTO objetDTO);

  ObjetDTO venduObject(ObjetDTO objetDTO);
}
