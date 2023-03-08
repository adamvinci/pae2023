package be.vinci.pae.dal;

import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.factory.TypeObjetFactory;
import be.vinci.pae.dal.services.DALService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link TypeObjetDAO}.
 */

public class TypeObjetDAOImpl implements TypeObjetDAO {

  @Inject
  private DALService dalService;

  @Inject
  private TypeObjetFactory typeObjetFactory;

  @Override
  public TypeObjetDTO getOne(int id) {
    String query = "SELECT libelle FROM projet.types_objets WHERE id_type = (?)";
    TypeObjetDTO typeObjetDTO = typeObjetFactory.getTypeObjet();
    try (PreparedStatement statement = dalService.preparedStatement(query)) {
      statement.setInt(1, id);
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (email does not exist)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            typeObjetDTO.setIdObjet(id);
            typeObjetDTO.setLibelle(set.getString(1));
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return typeObjetDTO;
  }

  /**
   * Retrieve the type of object in the database.
   *
   * @return a list of object type or null if there are none.
   */
  public List<TypeObjetDTO> getAll() {
    List<TypeObjetDTO> typeObjetDTOList = new ArrayList<>();
    try (PreparedStatement statement = dalService.preparedStatement("SELECT id_type,"
        + "libelle FROM projet.types_objets")) {
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (email does not exist)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            TypeObjetDTO typeObjetDTO = typeObjetFactory.getTypeObjet();
            typeObjetDTO.setIdObjet(Integer.parseInt(set.getString(1)));
            typeObjetDTO.setLibelle(set.getString(2));
            typeObjetDTOList.add(typeObjetDTO);
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return typeObjetDTOList;
  }
}
