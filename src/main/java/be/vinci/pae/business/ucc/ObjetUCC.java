package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import java.util.List;

/**
 * ObjetUCC acts as an orchestrator to allow {@link be.vinci.pae.ihm.ObjetRessource} and
 * {@link be.vinci.pae.dal.ObjectDAO} layers to communicate.
 */
public interface ObjetUCC {

  List<ObjetDTO> getAllObject();

  List<TypeObjetDTO> getAllObjectType();
}
