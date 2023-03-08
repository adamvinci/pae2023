package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.dal.ObjectDAO;
import be.vinci.pae.dal.TypeObjetDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link ObjetUCC}.
 */
public class ObjetUCCImpl implements ObjetUCC {

  @Inject
  private ObjectDAO dataService;

  @Inject
  private TypeObjetDAO typeObjetDAO;

  @Override
  public List<ObjetDTO> getAllObject() {

    return dataService.getAllObjet();
  }

  @Override
  public List<TypeObjetDTO> getAllObjectType() {
    return typeObjetDAO.getAll();
  }
}
