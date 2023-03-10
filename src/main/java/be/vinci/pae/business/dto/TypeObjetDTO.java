package be.vinci.pae.business.dto;


import be.vinci.pae.business.domaine.TypeObjetImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Used to define the type of object.
 */
@JsonDeserialize(as = TypeObjetImpl.class)
public interface TypeObjetDTO {

  /**
   * Returns the ID of the object.
   *
   * @return the ID of the object
   */
  int getIdObjet();

  /**
   * Sets the ID of the object.
   *
   * @param idObjet the ID to set
   */
  void setIdObjet(int idObjet);

  /**
   * Returns the label of the object.
   *
   * @return the label of the object
   */
  String getLibelle();

  /**
   * Sets the label of the object.
   *
   * @param libelle the label to set
   */
  void setLibelle(String libelle);

}
