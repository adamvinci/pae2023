package be.vinci.pae.dal;

import be.vinci.pae.buis.dto.UserDTO;
import be.vinci.pae.buis.factory.UserFactory;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Implementation of UserDataService.
 */
public class UserDataServiceImpl implements UserDataService {

  private Connection conn = null;
  @Inject
  private UserFactory userFactory;
  private PreparedStatement statement;

  /**
   * Connect to the database for each request.
   */
  public void connection() {
    try {
      conn = DriverManager.getConnection(Config.getProperty("url"), Config.getProperty("user"),
          Config.getProperty("dbPassword"));
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le server !");
      System.exit(1);
    }
  }

  /**
   * Disconnect from the database after each request.
   */

  public void disconnect() {
    try {

      conn.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserDTO getOne(String email) {
    UserDTO userDTO = userFactory.getUserDTO();
    connection();
    try {
      statement = conn.prepareStatement(
          "SELECT id_utilisateur,mot_de_passe,nom"
              + ",prenom,image,date_inscription,role,gsm"
              + " FROM projet.utilisateurs_inscrits WHERE email = (?)");
      statement.setString(1, email);
      try (ResultSet set = statement.executeQuery()) {

        while (set.next()) {
          if (set.getString(3).equals("null")) {
            return null;
          } else {
            initUser(userDTO, email, set.getString(2), set.getString(3),
                set.getString(4),
                set.getString(5), set.getDate(6).toLocalDate(), set.getString(7),
                set.getString(8), set.getInt(1));
          }

        }
      }

    } catch (SQLException e) {
      System.out.println("\n" + e.getMessage().split("\n")[0] + "\n");
    }
    disconnect();
    userDTO.setEmail(email);
    return userDTO;
  }

  @Override
  public UserDTO getOne(int id) {
    UserDTO userDTO = userFactory.getUserDTO();
    connection();
    try {
      statement = conn.prepareStatement(
          "SELECT email,mot_de_passe,nom"
              + ",prenom,image,date_inscription,role,gsm"
              + " FROM projet.utilisateurs_inscrits WHERE id_utilisateur = (?)");
      statement.setInt(1, id);
      try (ResultSet set = statement.executeQuery()) {
        while (set.next()) {
          if (set.getString(3).equals("null")) {
            return null;
          } else {
            initUser(userDTO, set.getString(1), set.getString(2), set.getString(3),
                set.getString(4),
                set.getString(5), set.getDate(6).toLocalDate(), set.getString(7),
                set.getString(8), id);
          }

        }
      }

    } catch (SQLException e) {
      System.out.println("\n" + e.getMessage().split("\n")[0] + "\n");
    }
    disconnect();
    return userDTO;
  }

  /**
   * init a UserDto from resultset got from the database (to pass the cpd duplications check).
   *
   * @param userDTO  the user in which the data will be added
   * @param email    the email we have to search in the database or the email corresponding to the
   *                 id in the database
   * @param id       the id we have to search in the database or the id corresponding to the email
   *                 in the database
   * @param password the password corresponding to the email/id in the database
   * @param nom      the name corresponding to the email/id in the database
   * @param prenom   the prenom corresponding to the email/id in the database
   * @param image    the image corresponding to the email/id in the database
   * @param date     the date corresponding to the email/id in the database
   * @param role     the role corresponding to the email/id in the database
   * @param gsm      the gsm corresponding to the email/id in the database
   */
  public void initUser(UserDTO userDTO, String email, String password, String nom, String prenom,
      String image,
      LocalDate date, String role, String gsm, int id) {
    userDTO.setEmail(email);
    userDTO.setPassword(password);
    userDTO.setNom(nom);
    userDTO.setPrenom(prenom);
    userDTO.setImage(image);
    userDTO.setDateInscription(date);
    userDTO.setRole(role);
    userDTO.setGsm(gsm);
    userDTO.setId(id);
  }
}