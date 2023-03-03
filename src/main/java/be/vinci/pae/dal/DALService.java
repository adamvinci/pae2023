package be.vinci.pae.dal;

import java.sql.PreparedStatement;

public interface DALService {

  PreparedStatement preparedStatement(String query);
}
