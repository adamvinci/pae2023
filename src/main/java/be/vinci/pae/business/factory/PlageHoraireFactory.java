package be.vinci.pae.business.factory;

import be.vinci.pae.business.dto.PlageHoraireDTO;

/**
 * Provide an instance of {@link PlageHoraireDTO}.
 */
public interface PlageHoraireFactory {

  /**
   * Returns a new {@link PlageHoraireDTO} object .
   *
   * @return a new {@link PlageHoraireDTO} object
   */
  PlageHoraireDTO getplageHoraireDTO();
}
