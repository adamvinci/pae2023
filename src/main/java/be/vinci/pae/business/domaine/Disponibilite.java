package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import java.time.LocalDate;

/**
 * * Implements directly {@link DisponibiliteDTO} as it does not need a business class with business
 * method.
 */
public class Disponibilite implements DisponibiliteDTO {

  private int id;

  private LocalDate date;

  private String plage;

  @Override
  public LocalDate getDate() {
    return date;
  }

  @Override
  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public String getPlage() {
    return plage;
  }


  @Override
  public void setPlage(String plage) {
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
