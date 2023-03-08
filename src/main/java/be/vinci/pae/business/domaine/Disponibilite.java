package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.dto.PlageHoraireDTO;
import java.time.LocalDate;

/**
 * Implementation of DisponibiliteDTO.
 */
public class Disponibilite implements DisponibiliteDTO {

  private int id;
  private LocalDate date;

  private PlageHoraireDTO plage;

  @Override
  public LocalDate getDate() {
    return date;
  }

  @Override
  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public PlageHoraireDTO getPlage() {
    return plage;
  }


  @Override
  public void setPlage(PlageHoraireDTO plage) {
    this.plage = plage;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }
}
