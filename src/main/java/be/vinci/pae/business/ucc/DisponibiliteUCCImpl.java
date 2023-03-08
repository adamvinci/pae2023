package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.dal.DisponibiliteDAO;
import jakarta.inject.Inject;
import java.util.List;

public class DisponibiliteUCCImpl implements DisponibiliteUCC {


  @Inject
  private DisponibiliteDAO disponibiliteDAO;

  @Override
  public List<DisponibiliteDTO> getDisponibilite() {
    return disponibiliteDAO.getAll();
  }
}
