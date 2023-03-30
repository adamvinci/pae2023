package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.NotificationDTO;
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
   * Accepts an object represented by the given ObjetDTO object.
   *
   * @param objetDTO        the object to be accepted
   * @param notificationDTO the notification to link with the object
   * @return the changed ObjetDTO
   */
  ObjetDTO accepterObjet(ObjetDTO objetDTO, NotificationDTO notificationDTO);


  /**
   * Refuses an object represented by the given ObjetDTO object.
   *
   * @param objetDTO        the object to be refused
   * @param message         of the notification provided by Mr.Riez
   * @param notificationDTO to save the reason of the refusal
   * @return the changed ObjetDTO
   */
  ObjetDTO refuserObject(ObjetDTO objetDTO, String message, NotificationDTO notificationDTO);

  /**
   * Deposits an object represented by the given ObjetDTO object.
   *
   * @param objetDTO the object to be deposited
   * @return the changed ObjetDTO
   */
  ObjetDTO depotObject(ObjetDTO objetDTO);

  /**
   * Sells an object represented by the given ObjetDTO object.
   *
   * @param objetDTO the object to sell
   * @return the changed ObjetDTO
   */
  ObjetDTO mettreEnVente(ObjetDTO objetDTO);

  /**
   * Sold an object.
   *
   * @param objetDTO the solded object.
   * @return the changed ObjetDTO
   */
  ObjetDTO vendreObject(ObjetDTO objetDTO);

  /**
   * Retrieve the object linked to the id.
   *
   * @param id of the object to retrieve
   * @return the object found.
   */
  ObjetDTO getOne(int id);
}
