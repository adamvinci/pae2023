package be.vinci.pae.business.dto;

import be.vinci.pae.business.domaine.PlageHoraire;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Used to create an argument in DisponibiliteDTO.
 */
@JsonDeserialize(as = PlageHoraire.class)
public interface PlageHoraireDTO {

  int getId();

  void setId(int id);

  String getPlage();

  void setPlage(String plage);

}
