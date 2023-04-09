package be.vinci.pae.dal;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.factory.DisponibiliteFactory;
import be.vinci.pae.business.factory.ObjetFactory;
import be.vinci.pae.business.factory.TypeObjetFactory;
import be.vinci.pae.dal.services.DALService;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
            + "p.plage,o.version FROM projet.objets o,projet.disponibilites d "
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
            setObjetDTOFromResultSet(set, objetDTO);
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
  public ObjetDTO updateObjectState(ObjetDTO objetDTO) {
    String query = "UPDATE projet.objets SET etat = ?, date_acceptation = ?, "
        + "date_depot = ?, date_retrait = ?, date_vente = ?"
        + ", localisation = ?,prix_vente = ? , version = version+1 WHERE id_objet = ? "
        + "AND version = ? RETURNING *";
    try (PreparedStatement statement = dalService.preparedStatement(query)) {
      statement.setString(1, objetDTO.getEtat());

      if (objetDTO.getDate_acceptation() != null) {
        statement.setDate(2, java.sql.Date.valueOf(objetDTO.getDate_acceptation()));
      } else {
        statement.setDate(2, null);
      }
      if (objetDTO.getDate_depot() != null) {
        statement.setDate(3, java.sql.Date.valueOf(objetDTO.getDate_depot()));
      } else {
        statement.setDate(3, null);
      }
      if (objetDTO.getDate_retrait() != null) {
        statement.setDate(4, java.sql.Date.valueOf(objetDTO.getDate_retrait()));
      } else {
        statement.setDate(4, null);
      }
      if (objetDTO.getDate_vente() != null) {
        statement.setDate(5, java.sql.Date.valueOf(objetDTO.getDate_vente()));
      } else {
        statement.setDate(5, null);
      }

      statement.setString(6, objetDTO.getLocalisation());
      if (objetDTO.getPrix() != null) {
        statement.setDouble(7, objetDTO.getPrix());
      } else {
        statement.setNull(7, java.sql.Types.DOUBLE);
      }

      statement.setInt(8, objetDTO.getIdObjet());
      statement.setInt(9, objetDTO.getNoVersion());
      try (ResultSet rs = statement.executeQuery()) {
        if (!rs.isBeforeFirst()) {
          throw new NoSuchElementException();
        }
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }
    objetDTO.setNoVersion(objetDTO.getNoVersion() + 1);
    return objetDTO;
  }

  /**
   * Fill an objetDTO with the resultset of a query.
   *
   * @param set      the response of a query.
   * @param objetDTO object to fill.
   * @throws SQLException in case of problem with the database
   */
  public void setObjetDTOFromResultSet(ResultSet set, ObjetDTO objetDTO) throws SQLException {
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
    disponibiliteDTO.setPlage(set.getString(17));
    objetDTO.setDisponibilite(disponibiliteDTO);
    objetDTO.setNoVersion(set.getInt(18));
  }

  /**
   * Retrieve the object linked to the id.
   *
   * @param id of object to search
   * @return the object or null
   */
  public ObjetDTO getOne(int id) {
    ObjetDTO objetDTO = objetFactory.getObjet();
    String query = "SELECT o.id_objet,o.gsm,o.photo,o.description,o.etat,"
        + "o.date_acceptation,o.localisation,o.date_depot,"
        + "o.date_retrait,o.prix_vente,"
        + "o.date_vente,o.utilisateur,tob.id_type,"
        + "tob.libelle,d.id_disponibilite,d.date_disponibilite,"
        + "p.plage,o.version FROM projet.objets o,projet.disponibilites d "
        + ",projet.plages_horaires p ,projet.types_objets tob "
        + "  WHERE id_objet = (?)  ";
    try (PreparedStatement statement = dalService.preparedStatement(query)) {
      statement.setInt(1, id);
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (none objet)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            setObjetDTOFromResultSet(set, objetDTO);
          }

        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return objetDTO;
  }


}