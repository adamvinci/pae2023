package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.Disponibilite;
import be.vinci.pae.business.dto.DisponibiliteDTO;

/**
 * Implementation of {@link DisponibiliteFactory}.
 */
public class DisponibiliteFactoryImpl implements DisponibiliteFactory {

  /**
   * Returns a new {@link DisponibiliteDTO} object .
   *
   * @return a new {@link DisponibiliteDTO} object
   */
  @Override
  public DisponibiliteDTO getDisponibilite() {
    return new Disponibilite();
  }

}
