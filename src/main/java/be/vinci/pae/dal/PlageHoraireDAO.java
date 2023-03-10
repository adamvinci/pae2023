package be.vinci.pae.dal;

import be.vinci.pae.business.dto.PlageHoraireDTO;

/**
 * PlageHoraireDAO purpose is to communicate with the database.
 */
public interface PlageHoraireDAO {

  /**
   * Retrieve the 'plage' in the database.
   *
   * @return a list of 'plage' or null if there are none.
   */
  PlageHoraireDTO getOne(int id);
}
