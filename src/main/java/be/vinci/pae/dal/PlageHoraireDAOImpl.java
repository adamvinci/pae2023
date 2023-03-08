package be.vinci.pae.dal;

import be.vinci.pae.business.dto.PlageHoraireDTO;
import be.vinci.pae.business.factory.PlageHoraireFactory;
import be.vinci.pae.dal.services.DALService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlageHoraireDAOImpl implements PlageHoraireDAO {

  @Inject
  private DALService dalService;

  @Inject
  private PlageHoraireFactory plageHoraireFactory;

  @Override
  public PlageHoraireDTO getOne(int id) {

    PlageHoraireDTO plageHoraireDTO = plageHoraireFactory.getplageHoraireDTO();
    try (PreparedStatement statement = dalService.preparedStatement(
        "SELECT plage FROM projet.plages_horaires WHERE id_plage_horaire = (?)")) {
      statement.setInt(1, id);
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            plageHoraireDTO.setId(id);
            plageHoraireDTO.setPlage(set.getString(1));
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return plageHoraireDTO;
  }

}
