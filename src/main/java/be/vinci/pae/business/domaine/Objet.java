package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.ObjetDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Class with the business method of object.
 */
@JsonDeserialize(as = ObjetImpl.class)
public interface Objet extends ObjetDTO {

  /**
   * Indicates whether this object has been accepted.
   *
   * @return True if the object has been accepted; false otherwise.
   */
  Boolean accepterObjet();

  /**
   * Indicates whether this object has been refused.
   *
   * @return True if the object has been refused; false otherwise.
   */
  Boolean refuserObjet();

  /**
   * Redirect the object to the good method depending on its localisation
   *
   * @return refuserObjet or accepterObjet
   */
  Boolean deposer();

  /**
   * Indicates whether this object has been deposited.
   *
   * @return True if the object has been deposited; false otherwise.
   */
  Boolean deposerEnMagasin();

  /**
   * Indicates whether this object has been deposited.
   *
   * @return True if the object has been deposited; false otherwise.
   */
  Boolean deposerEnAtelier();

  /**
   * Indicates whether this object is available for sale.
   *
   * @return True if the object is available for sale; false otherwise.
   */
  Boolean mettreEnVente();

  /**
   * Indicates whether this object has been sold.
   *
   * @return True if the object has been sold; false otherwise.
   */
  Boolean vendreObjet();
}
