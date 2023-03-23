package be.vinci.pae.dal;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import java.util.List;

/**
 * DisponibiliteDAO purpose is to communicate with the disponibilite table in the database.
 */
public interface DisponibiliteDAO {

  /**
   * Get the availability corresponding to the id in the DB.
   *
   * @param id to search
   * @return the availability matching the id or else null
   */
  DisponibiliteDTO getOne(int id);

  /**
   * Get All the availability.
   *
   * @return a list of availability or else null
   */
  List<DisponibiliteDTO> getAll();
}
