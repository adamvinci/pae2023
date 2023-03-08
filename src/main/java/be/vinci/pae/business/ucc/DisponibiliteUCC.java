package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import java.util.List;

/**
 * DisponibiliteUCC acts as an orchestrator to allow {@link be.vinci.pae.ihm.DisponibiliteRessource}
 * and {@link be.vinci.pae.dal.DisponibiliteDAO} layers to communicate.
 */
public interface DisponibiliteUCC {

  List<DisponibiliteDTO> getDisponibilite();
}
