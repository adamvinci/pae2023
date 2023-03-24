package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.dal.DisponibiliteDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.FatalException;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link DisponibiliteUCC}.
 */
public class DisponibiliteUCCImpl implements DisponibiliteUCC {


  @Inject
  private DisponibiliteDAO disponibiliteDAO;

  @Inject
  private DALTransaction dal;

  @Override
  public List<DisponibiliteDTO> getDisponibilite() {
    try {
      dal.startTransaction();
    List<DisponibiliteDTO> disponibiliteDTOS = disponibiliteDAO.getAll();
      dal.commitTransaction();
      return disponibiliteDTOS;

    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw new FatalException(e);
    }


  }

  @Override
  public DisponibiliteDTO getOne(int id) {
    try {
      dal.startTransaction();
      return disponibiliteDAO.getOne(id);
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw new FatalException(e);
    } finally {
      dal.commitTransaction();
    }
  }


}
