package be.vinci.pae.dal;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.factory.DisponibiliteFactory;
import be.vinci.pae.business.factory.ObjetFactory;
import be.vinci.pae.business.factory.TypeObjetFactory;
import be.vinci.pae.dal.services.DALService;
import be.vinci.pae.utils.FatalException;
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
  private TypeObjetFactory typeObjetFactory;
  @Inject
  private DisponibiliteFactory disponibiliteFactory;

  @Override
  public List<ObjetDTO> getAllObjet() {
    List<ObjetDTO> objetDTOList = new ArrayList<>();
    String query =
        "SELECT o.id_objet,o.gsm,o.photo,o.description,o.etat,"
            + "o.date_acceptation,o.localisation,o.date_depot,"
            + "o.date_retrait,o.prix_vente,"
            + "o.date_vente,o.utilisateur,tob.id_type,"
            + "tob.libelle,d.id_disponibilite,d.date_disponibilite,"
            + "p.plage FROM projet.objets o,projet.disponibilites d "
            + ",projet.plages_horaires p ,projet.types_objets tob "
            + "WHERE  tob.id_type = o.type  "
            + "AND o.disponibilite = d.id_disponibilite AND d.plage = p.id_plage_horaire";
    try (PreparedStatement statement = dalService.preparedStatement(query)) {
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (none objet)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            ObjetDTO objetDTO = objetFactory.getObjet();

            objetDTO.setIdObjet(Integer.parseInt(set.getString(1)));
            objetDTO.setGsm(set.getString(2));
            objetDTO.setPhoto(set.getString(3));
            objetDTO.setDescription(set.getString(4));
            objetDTO.setEtat(set.getString(5));
            if (set.getString(6) != null) {
              objetDTO.setDate_acceptation(LocalDate.parse(set.getString(6)));
            }
            objetDTO.setLocalisation(set.getString(7));
            if (set.getString(8) != null) {
              objetDTO.setDate_depot(LocalDate.parse(set.getString(8)));
            }
            if (set.getString(9) != null) {
              objetDTO.setDate_retrait(LocalDate.parse(set.getString(9)));
            }
            if (set.getString(10) != null) {
              objetDTO.setPrix(Double.parseDouble(set.getString(10)));
            }

            if (set.getString(11) != null) {
              objetDTO.setDate_vente(LocalDate.parse(set.getString(11)));
            }
            if (set.getString(12) != null) {
              objetDTO.setUtilisateur(Integer.valueOf(set.getString(12)));
            }

            TypeObjetDTO typeObjetDTO = typeObjetFactory.getTypeObjet();
            typeObjetDTO.setIdObjet(set.getInt(13));
            typeObjetDTO.setLibelle(set.getString(14));
            objetDTO.setTypeObjet(typeObjetDTO);

            DisponibiliteDTO disponibiliteDTO = disponibiliteFactory.getDisponibilite();
            disponibiliteDTO.setId(set.getInt(15));
            disponibiliteDTO.setDate(LocalDate.parse(set.getString(16)));
            disponibiliteDTO.setPlage(set.getString(16));
            objetDTO.setDisponibilite(disponibiliteDTO);

            objetDTOList.add(objetDTO);
          }
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
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
      throw new FatalException(e);
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