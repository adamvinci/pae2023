package be.vinci.pae.business.dto;

import be.vinci.pae.business.domaine.Disponibilite;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

/**
 * Used to define the Availability of the 'aidant' and 'responsable'.
 */
@JsonDeserialize(as = Disponibilite.class)
public interface DisponibiliteDTO {

  LocalDate getDate();

  void setDate(LocalDate date);

  PlageHoraireDTO getPlage();

  void setPlage(PlageHoraireDTO plage);

  int getId();

  void setId(int id);

}
