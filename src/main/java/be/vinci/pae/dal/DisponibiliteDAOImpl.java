package be.vinci.pae.dal;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.factory.DisponibiliteFactory;
import be.vinci.pae.dal.services.DALService;
import be.vinci.pae.utils.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link DisponibiliteDAO}.
 */
public class DisponibiliteDAOImpl implements DisponibiliteDAO {

  @Inject
  private DALService dalService;

  @Inject
  private DisponibiliteFactory disponibiliteFactory;


  @Override
  public DisponibiliteDTO getOne(int id) {
    String query = "SELECT d.date_disponibilite,p.plage "
        + "FROM projet.disponibilites d,"
        + "projet.plages_horaires p WHERE "
        + "d.plage = p.id_plage_horaire AND id_disponibilite = (?)";
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
            disponibiliteDTO.setPlage(set.getString(2));
          }
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return disponibiliteDTO;
  }

  @Override
  public List<DisponibiliteDTO> getAll() {
    List<DisponibiliteDTO> disponibiliteDTOS = new ArrayList<>();
    try (PreparedStatement statement = dalService.preparedStatement(
        "SELECT d.id_disponibilite,"
            + "d.date_disponibilite,p.plage "
            + "FROM projet.plages_horaires p,"
            + "projet.disponibilites d WHERE d.plage = p.id_plage_horaire ")) {
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (email does not exist)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            DisponibiliteDTO disponibiliteDTO = disponibiliteFactory.getDisponibilite();
            disponibiliteDTO.setId(Integer.parseInt(set.getString(1)));
            disponibiliteDTO.setDate(LocalDate.parse(set.getString(2)));
            disponibiliteDTO.setPlage(set.getString(3));
            disponibiliteDTOS.add(disponibiliteDTO);
          }
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return disponibiliteDTOS;
  }
}

