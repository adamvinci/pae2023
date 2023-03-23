package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.dal.ObjectDAO;
import be.vinci.pae.dal.TypeObjetDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.FatalException;
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

  @Inject
  private DALTransaction dal;

  @Override
  public List<ObjetDTO> getAllObject() {
    try {
      dal.startTransaction();
      return dataService.getAllObjet();
    }catch (FatalException e){
      dal.rollBackTransaction();
      throw new FatalException(e);
    }finally {
      dal.commitTransaction();
    }
  }

  @Override
  public List<TypeObjetDTO> getAllObjectType() {
    try {
      dal.startTransaction();
      return typeObjetDAO.getAll();
    }catch (FatalException e){
      dal.rollBackTransaction();
      throw new FatalException(e);
    }finally {
      dal.commitTransaction();
    }
  }

  @Override
  public String getPicture(int id) {
    try {
      dal.startTransaction();
      return dataService.getPicture(id);
    }catch (FatalException e){
      dal.rollBackTransaction();
      throw new FatalException(e);
    }finally {
      dal.commitTransaction();
    }
  }
}
