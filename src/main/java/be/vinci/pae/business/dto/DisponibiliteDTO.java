package be.vinci.pae.business.dto;

import be.vinci.pae.business.domaine.Disponibilite;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

/**
 * Used to define the Availability of the 'aidant' and 'responsable'.
 */
@JsonDeserialize(as = Disponibilite.class)
public interface DisponibiliteDTO {

  /**
   * Returns the date associated with this object.
   *
   * @return the date associated with this object
   */
  LocalDate getDate();

  /**
   * Sets the date associated with this object.
   *
   * @param date the new date to associate with this object
   */
  void setDate(LocalDate date);

  /**
   * Returns the PlageHoraireDTO object associated with this object.
   *
   * @return the PlageHoraireDTO object associated with this object
   */
  String getPlage();

  /**
   * Sets the PlageHoraireDTO object associated with this object.
   *
   * @param plage the new PlageHoraireDTO object to associate with this object
   */
  void setPlage(String plage);

  /**
   * Returns the ID of this object.
   *
   * @return the ID of this object
   */
  int getId();

  /**
   * Sets the ID of this object.
   *
   * @param id the new ID to assign to this object
   */
  void setId(int id);

}
