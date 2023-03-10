package be.vinci.pae.business.dto;

import be.vinci.pae.business.domaine.PlageHoraire;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Used to create an argument in DisponibiliteDTO.
 */
@JsonDeserialize(as = PlageHoraire.class)
public interface PlageHoraireDTO {

  /**
   * Returns the ID of the object.
   *
   * @return the ID of the object
   */
  int getId();

  /**
   * Sets the ID of the object.
   *
   * @param id the ID to set
   */
  void setId(int id);

  /**
   * Returns the time slot of the object.
   *
   * @return the time slot of the object
   */
  String getPlage();

  /**
   * Sets the time slot of the object.
   *
   * @param plage the time slot to set
   */
  void setPlage(String plage);

}
