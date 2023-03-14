package be.vinci.pae.business.factory;

import be.vinci.pae.business.dto.DisponibiliteDTO;

/**
 * Provide an instance of {@link DisponibiliteDTO}.
 */
public interface DisponibiliteFactory {

  /**
   * Returns a new {@link DisponibiliteDTO} object .
   *
   * @return a new {@link DisponibiliteDTO} object
   */
  DisponibiliteDTO getDisponibilite();
}
