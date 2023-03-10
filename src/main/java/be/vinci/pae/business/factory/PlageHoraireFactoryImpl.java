package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.PlageHoraire;
import be.vinci.pae.business.dto.PlageHoraireDTO;

/**
 * Implementation of {@link PlageHoraireFactory}.
 */
public class PlageHoraireFactoryImpl implements PlageHoraireFactory {

  @Override
  public PlageHoraireDTO getplageHoraireDTO() {
    return new PlageHoraire();
  }

}
