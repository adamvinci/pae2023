package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.PlageHoraireDTO;
import java.util.Arrays;

/**
 * Implements directly {@link PlageHoraireDTO} as it does not need a business class with business
 * method.
 */
public class PlageHoraire implements PlageHoraireDTO {


  private static final String[] POSSIBLE_PLAGE = {"matin", "apres midi"};
  private int id;
  private String plage;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getPlage() {
    return plage;
  }

  @Override
  public void setPlage(String plage) {
    this.plage = Arrays.stream(POSSIBLE_PLAGE).filter(s -> s.equals(plage)).findFirst()
        .orElse(null);
  }
}
