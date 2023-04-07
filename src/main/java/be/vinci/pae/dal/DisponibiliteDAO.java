package be.vinci.pae.dal;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import java.time.LocalDate;
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

  /**
   * Verify if this disponibility already exist or not.
   *
   * @param disponibiliteDTO to verify
   * @return the disponiblity or null
   */
  boolean disponibilityExist(DisponibiliteDTO disponibiliteDTO);

  /**
   * Create one disponibilty.
   *
   * @param disponibiliteDTO to create
   * @return the created disponibility
   */
  DisponibiliteDTO createOne(DisponibiliteDTO disponibiliteDTO);

  /**
   * Return the id of the plage.
   *
   * @param plage to get the id from
   * @return the id to use as a foreign key
   */
  int getPlageId(String plage);
}
