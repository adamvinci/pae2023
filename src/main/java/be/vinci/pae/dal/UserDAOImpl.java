package be.vinci.pae.dal;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.UserFactory;
import be.vinci.pae.dal.services.DALService;
import be.vinci.pae.utils.exception.ConflictException;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Implementation of {@link UserDAO}.
 */
public class UserDAOImpl implements UserDAO {


  @Inject
  private UserFactory userFactory;
  @Inject
  private DALService dalService;


  @Override
  public UserDTO getOne(String email) {

    UserDTO userDTO = userFactory.getUserDTO();
    try (PreparedStatement statement = dalService.preparedStatement(
        "SELECT id_utilisateur,mot_de_passe,nom" + ",prenom,image,date_inscription,role,gsm,version"
            + " FROM projet.utilisateurs_inscrits WHERE email = (?)")) {
      statement.setString(1, email);
      try (ResultSet set = statement.executeQuery()) {
        // check if resultset is empty (email does not exist)
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            initUser(userDTO, email, set.getString(2), set.getString(3), set.getString(4),
                set.getString(5), set.getDate(6).toLocalDate(), set.getString(7),
                set.getString(8),
                set.getInt(1), set.getInt(9));
          }
        }
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }

    return userDTO;
  }

  @Override
  public UserDTO getOne(int id) {
    UserDTO userDTO = userFactory.getUserDTO();
    try (PreparedStatement statement = dalService.preparedStatement(
        "SELECT email,mot_de_passe,nom" + ",prenom,image,date_inscription,role,gsm,version"
            + " FROM projet.utilisateurs_inscrits WHERE id_utilisateur = (?)")) {
      statement.setInt(1, id);
      try (ResultSet set = statement.executeQuery()) {
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            initUser(userDTO, set.getString(1), set.getString(2), set.getString(3),
                set.getString(4), set.getString(5), set.getDate(6).toLocalDate(),
                set.getString(7),
                set.getString(8), id, set.getInt(9));

          }
        }
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }

    return userDTO;
  }


  @Override
  public UserDTO createOne(UserDTO user) {
    UserDTO userDTO = userFactory.getUserDTO();

    try (PreparedStatement statement = dalService.preparedStatement(
        "INSERT INTO projet.utilisateurs_inscrits VALUES "
            + "(DEFAULT,?,?,?,?,?,?,?,?,?) RETURNING *;")) {
      statement.setString(1, user.getEmail());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getNom());
      statement.setString(4, user.getPrenom());
      if (user.getImage() != null) {
        statement.setString(5, user.getImage());
      } else {
        statement.setString(5, "");
      }
      statement.setDate(6, Date.valueOf(LocalDate.now()));
      statement.setString(7, "membre");
      statement.setString(8, user.getGsm());
      statement.setInt(9, 1);
      try (ResultSet set = statement.executeQuery()) {
        if (!set.isBeforeFirst()) {

          return null;
        } else {
          while (set.next()) {
            initUser(userDTO, set.getString(2), set.getString(3), set.getString(4),
                set.getString(5), set.getString(6), set.getDate(7).toLocalDate(),
                set.getString(8),
                set.getString(9), set.getInt(1), set.getInt(10));

          }
        }
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }

    return userDTO;
  }

  @Override
  public List<UserDTO> getAll() {
    ArrayList<UserDTO> usersList = new ArrayList<>();
    try (PreparedStatement statement = dalService.preparedStatement(
        "SELECT email,mot_de_passe,nom"
            + ",prenom,image,date_inscription,role,gsm,id_utilisateur,version"
            + " FROM projet.utilisateurs_inscrits")) {
      try (ResultSet set = statement.executeQuery()) {
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            UserDTO userDTO = userFactory.getUserDTO();
            initUser(userDTO, set.getString(1), set.getString(2), set.getString(3),
                set.getString(4), set.getString(5), set.getDate(6).toLocalDate(),
                set.getString(7),
                set.getString(8), set.getInt(9), set.getInt(10));
            usersList.add(userDTO);
          }
        }
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return usersList;
  }

  @Override
  public boolean update(UserDTO userToChange) {
    try (PreparedStatement statement = dalService.preparedStatement(
        "UPDATE projet.utilisateurs_inscrits"
            + " SET role = ?"
            + " ,email = ?"
            + " ,nom = ?"
            + " ,prenom = ?"
            + " ,mot_de_passe = ?"
            + " ,date_inscription = ?"
            + " ,gsm = ?"
            + " ,image = ? ,version = version+1"
            + " WHERE id_utilisateur = ? AND version = ? RETURNING *")) {

      statement.setString(1, userToChange.getRole());
      statement.setString(2, userToChange.getEmail());
      statement.setString(3, userToChange.getNom());
      statement.setString(4, userToChange.getPrenom());
      statement.setString(5, userToChange.getPassword());
      statement.setDate(6, Date.valueOf(LocalDate.now()));
      statement.setString(7, userToChange.getGsm());
      statement.setString(8, userToChange.getImage());
      statement.setInt(9, userToChange.getId());
      statement.setInt(10, userToChange.getVersion());
      try (ResultSet rs = statement.executeQuery()) {
        if (!rs.isBeforeFirst()) {
          throw new NoSuchElementException();
        } else {
          return true;

        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public String getPicture(int id) {
    String path = null;
    String query = "SELECT image FROM projet.utilisateurs_inscrits WHERE id_utilisateur = (?)";
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

  /**
   * init a UserDto from resultset got from the database (to pass the cpd duplications check).
   *
   * @param userDTO   the user in which the data will be added
   * @param email     the email we have to search in the database or the email corresponding to the
   *                  id in the database
   * @param id        the id we have to search in the database or the id corresponding to the email
   *                  in the database
   * @param password  the password corresponding to the email/id in the database
   * @param nom       the name corresponding to the email/id in the database
   * @param prenom    the prenom corresponding to the email/id in the database
   * @param image     the image corresponding to the email/id in the database
   * @param date      the date corresponding to the email/id in the database
   * @param role      the role corresponding to the email/id in the database
   * @param gsm       the gsm corresponding to the email/id in the database
   * @param noVersion the version number of the current row
   */
  public void initUser(UserDTO userDTO, String email, String password, String nom, String prenom,
      String image, LocalDate date, String role, String gsm, int id, int noVersion) {
    userDTO.setEmail(email);
    userDTO.setPassword(password);
    userDTO.setNom(nom);
    userDTO.setPrenom(prenom);
    userDTO.setImage(image);
    userDTO.setDateInscription(date);
    userDTO.setRole(role);
    userDTO.setGsm(gsm);
    userDTO.setId(id);
    userDTO.setVersion(noVersion);
  }
}
