package be.vinci.pae.dal;

import java.sql.PreparedStatement;

/**
 * DALService purpose is to connect to the database once by preparing and sending statement.
 */
public interface DALService {

  PreparedStatement preparedStatement(String query);
}
