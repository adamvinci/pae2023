package be.vinci.pae.dal.services;

import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation of DALService.
 */
public class DALServiceImpl implements DALService {

  private Connection conn;

  /**
   * Connect to the database once.
   */
  public DALServiceImpl() {
    try {
      conn = DriverManager.getConnection(Config.getProperty("url"), Config.getProperty("user"),
          Config.getProperty("dbPassword"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Prepare the statement received by the DAO.
   *
   * @param query to put in the prepared statement
   * @return the prepared statement
   */
  @Override
  public PreparedStatement preparedStatement(String query) {
    PreparedStatement statement;
    try {
      statement = conn.prepareStatement(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return statement;
  }

}
