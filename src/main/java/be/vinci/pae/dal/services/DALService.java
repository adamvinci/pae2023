package be.vinci.pae.dal.services;

import java.sql.PreparedStatement;

/**
 * DALService purpose is to connect to the database once by preparing and sending statement.
 */
public interface DALService {
  
  /**
   * Prepare the statement received by the DAO.
   *
   * @param query to put in the prepared statement
   * @return the prepared statement
   */
  PreparedStatement preparedStatement(String query);

}
