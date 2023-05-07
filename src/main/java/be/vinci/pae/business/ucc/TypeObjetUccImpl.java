package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.dal.TypeObjetDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link TypeObjetUcc}.
 */
public class TypeObjetUccImpl implements TypeObjetUcc {

  @Inject
  private TypeObjetDAO typeObjetDAO;

  @Inject
  private DALTransaction dal;

  @Override
  public List<TypeObjetDTO> getAllObjectType() {
    try {
      dal.startTransaction();
      return typeObjetDAO.getAll();
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public TypeObjetDTO getOneType(int id) {
    try {
      dal.startTransaction();
      return typeObjetDAO.getOne(id);
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }
}
