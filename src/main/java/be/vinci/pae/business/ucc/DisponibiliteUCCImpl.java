package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.dal.DisponibiliteDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.exception.ConflictException;
import be.vinci.pae.utils.exception.FatalException;
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
      return disponibiliteDTOS;

    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }


  }

  @Override
  public DisponibiliteDTO getOne(int id) {
    try {
      dal.startTransaction();
      return disponibiliteDAO.getOne(id);
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }


  @Override
  public DisponibiliteDTO createOne(DisponibiliteDTO disponibiliteDTO1) {
    try {
      dal.startTransaction();
      if (disponibiliteDAO.disponibilityExist(disponibiliteDTO1)) {
        throw new ConflictException("This disponibilty already exist");
      }
      DisponibiliteDTO disponibiliteDTO = disponibiliteDAO.createOne(disponibiliteDTO1);
      dal.commitTransaction();
      return disponibiliteDTO;
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    }
  }
}
