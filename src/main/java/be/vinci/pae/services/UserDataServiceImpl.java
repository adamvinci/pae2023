package be.vinci.pae.services;

import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataServiceImpl implements UserDataService {

  private Connection conn = null;
  @Inject
  private UserFactory userFactory;
  private PreparedStatement statement;

  /**
   * Connect to the database for each request
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
   * Disconnect from the database after each request
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
            userDTO.setId(set.getInt(1));
            userDTO.setPassword(set.getString(2));
            userDTO.setNom(set.getString(3));
            userDTO.setPrenom(set.getString(4));
            userDTO.setImage(set.getString(5));
            userDTO.setDateInscription(
                set.getDate(6).toLocalDate());
            userDTO.setRole(set.getString(7));
            userDTO.setGsm(set.getString(8));
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


}
