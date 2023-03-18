package be.vinci.pae.dal;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.ObjetFactory;
import be.vinci.pae.dal.services.DALService;
import jakarta.inject.Inject;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ObjectDAO}.
 */
public class ObjectDAOImpl implements ObjectDAO {

  @Inject
  private DALService dalService;
  @Inject
  private ObjetFactory objetFactory;
  @Inject
  private UserDAO userDAO;
  @Inject
  private TypeObjetDAO typeObjetDAO;
  @Inject
  private DisponibiliteDAO disponibiliteDAO;

  @Override
  public List<ObjetDTO> getAllObjet() {
    List<ObjetDTO> objetDTOList = new ArrayList<>();
    String query = "SELECT * FROM projet.objets";
    try (PreparedStatement statement = dalService.preparedStatement(query)) {
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (none objet)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            ObjetDTO objetDTO = objetFactory.getObjet();

            objetDTO.setIdObjet(Integer.parseInt(set.getString(1)));
            UserDTO userDTO = userDAO.getOne(set.getString(2));
            objetDTO.setUtilisateur(userDTO);
            objetDTO.setGsm(set.getString(3));
            TypeObjetDTO typeObjetDTO = typeObjetDAO.getOne(Integer.parseInt(set.getString(5)));
            objetDTO.setTypeObjet(typeObjetDTO);
            objetDTO.setDescription(set.getString(6));
            DisponibiliteDTO disponibiliteDTO = disponibiliteDAO.getOne(
                Integer.parseInt(set.getString(7)));
            objetDTO.setDisponibilite(disponibiliteDTO);
            objetDTO.setEtat(set.getString(8));
            if (set.getString(9) != null) {
              objetDTO.setDate_acceptation(LocalDate.parse(set.getString(9)));
            }
            objetDTO.setLocalisation(set.getString(10));
            if (set.getString(11) != null) {
              objetDTO.setDate_depot(LocalDate.parse(set.getString(11)));
            }
            if (set.getString(12) != null) {
              objetDTO.setDate_retrait(LocalDate.parse(set.getString(12)));
            }
            if (set.getString(13) != null) {
              objetDTO.setPrix(Double.parseDouble(set.getString(13)));
            }

            if (set.getString(14) != null) {
              objetDTO.setDate_vente(LocalDate.parse(set.getString(14)));
            }

            objetDTOList.add(objetDTO);
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return objetDTOList;
  }

  @Override
  public String getPicture(int id) {
    String path = null;
    String query = "SELECT photo FROM projet.objets WHERE id_objet = (?)";
    try (PreparedStatement statement = dalService.preparedStatement(query)) {
      statement.setInt(1, id);
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (none objet)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            path = set.getString(1);
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return path;
  }

  @Override
  public boolean updateObjectState(ObjetDTO objetDTO) {
    String query = "UPDATE projet.objets SET etat = ?, date_acceptation = ?, date_depot = ?, date_retrait = ?, date_vente = ?, localisation = ? WHERE id_objet = ?";
    try (PreparedStatement statement = dalService.preparedStatement(query)) {
      statement.setString(1, objetDTO.getEtat());
      statement.setDate(2, Date.valueOf(objetDTO.getDate_acceptation()));
      statement.setDate(3, Date.valueOf(objetDTO.getDate_depot()));
      statement.setDate(4, Date.valueOf(objetDTO.getDate_retrait()));
      statement.setDate(5, Date.valueOf(objetDTO.getDate_vente()));
      statement.setString(6, objetDTO.getLocalisation());
      statement.setInt(7, objetDTO.getIdObjet());
      int rowsAffected = statement.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}