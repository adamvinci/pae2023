package be.vinci.pae.dal;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.dto.PlageHoraireDTO;
import be.vinci.pae.business.factory.DisponibiliteFactory;
import be.vinci.pae.dal.services.DALService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DisponibiliteDAOImpl implements DisponibiliteDAO {

  @Inject
  private DALService dalService;

  @Inject
  private DisponibiliteFactory disponibiliteFactory;

  @Inject
  private PlageHoraireDAO plageHoraireDAO;

  @Override
  public DisponibiliteDTO getOne(int id) {
    String query = "SELECT date_disponibilite,plage"
        + " FROM projet.disponibilites  WHERE id_disponibilite = (?)";
    DisponibiliteDTO disponibiliteDTO = disponibiliteFactory.getDisponibilite();

    try (PreparedStatement statement = dalService.preparedStatement(query)) {
      statement.setInt(1, id);
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (email does not exist)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            disponibiliteDTO.setId(id);
            disponibiliteDTO.setDate(LocalDate.parse(set.getString(1)));
            PlageHoraireDTO plageHoraireDTO = plageHoraireDAO.getOne(
                Integer.parseInt(set.getString(2)));
            disponibiliteDTO.setPlage(plageHoraireDTO);
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return disponibiliteDTO;
  }

  @Override
  public List<DisponibiliteDTO> getAll() {
    List<DisponibiliteDTO> disponibiliteDTOS = new ArrayList<>();
    try (PreparedStatement statement = dalService.preparedStatement("SELECT id_disponibilite,"
        + "date_disponibilite,plage FROM projet.disponibilites")) {
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (email does not exist)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            DisponibiliteDTO disponibiliteDTO = disponibiliteFactory.getDisponibilite();
            disponibiliteDTO.setId(Integer.parseInt(set.getString(1)));
            disponibiliteDTO.setDate(LocalDate.parse(set.getString(2)));
            PlageHoraireDTO plageHoraireDTO = plageHoraireDAO.getOne(
                Integer.parseInt(set.getString(3)));
            disponibiliteDTO.setPlage(plageHoraireDTO);
            disponibiliteDTOS.add(disponibiliteDTO);
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return disponibiliteDTOS;
  }
}

