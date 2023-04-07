package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * DisponibiliteUCC acts as an orchestrator to allow {@link be.vinci.pae.ihm.DisponibiliteRessource}
 * and {@link be.vinci.pae.dal.DisponibiliteDAO} layers to communicate.
 */
public interface DisponibiliteUCC {

  /**
   * Retrieve the availability of Mr.Riez and his 'aidant'.
   *
   * @return a list containing the availability
   */
  List<DisponibiliteDTO> getDisponibilite();

  /**
   * Retrieve one 'disponibility' of the database.
   *
   * @param id of the 'disponibility' to search
   * @return the disponibility
   */
  DisponibiliteDTO getOne(int id);


  /**
   * Create one disponibilty.
   *
   * @param disponibiliteDTO to create
   * @return the created disponibility
   */
  DisponibiliteDTO createOne(DisponibiliteDTO disponibiliteDTO);
}
