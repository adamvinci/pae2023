package be.vinci.pae.dal;

import be.vinci.pae.business.dto.PlageHoraireDTO;

/**
 * PlageHoraireDAO purpose is to communicate with the database.
 */
public interface PlageHoraireDAO {

  PlageHoraireDTO getOne(int id);
}
