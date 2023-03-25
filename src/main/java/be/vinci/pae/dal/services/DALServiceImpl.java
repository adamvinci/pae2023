package be.vinci.pae.dal.services;

import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.FatalException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Implementation of DALService.
 */
public class DALServiceImpl implements DALService, DALTransaction {

  private ThreadLocal<Connection> conn;

  private BasicDataSource db;

  /**
   * Connect to the database once.
   */
  public DALServiceImpl() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    this.conn = new ThreadLocal<>();
    this.db = initDB();
  }

  private BasicDataSource initDB() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl(Config.getProperty("url"));
    ds.setUsername(Config.getProperty("user"));
    ds.setPassword(Config.getProperty("dbPassword"));
    return ds;
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
      statement = conn.get().prepareStatement(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return statement;
  }

  /**
   *Starts a new transaction by getting a new connection from the database
   *and disabling auto-commit.

   *Throws a RuntimeException if a connection has already been acquired.

   * @throws FatalException if an SQL exception occurs while starting the transaction.
   */

  @Override
  public void startTransaction() {
    try {
      if (conn.get() != null) {
        throw new RuntimeException("conn deja utilise");
      }
      Connection connex = db.getConnection();
      conn.set(connex);
      connex.setAutoCommit(false);
    } catch (SQLException exception) {
      exception.printStackTrace();
      throw new FatalException(exception);
    }
  }
  /**
   *Commits the current transaction to the database.

   *Throws a RuntimeException if no connection is available.
   *Any SQLException encountered during the commit will be wrapped in a FatalException and rethrown.
   *At the end, the connection is closed.
   */

  @Override
  public void commitTransaction() {
    try {
      Connection connex = null;
      if ((connex = conn.get()) == null) {
        throw new RuntimeException("Pas de connexion");
      }
      connex.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    } finally {
      fermerConnexion();
    }
  }
  /**
   *Rolls back the current transaction associated with the connection.
   *
   *@throws RuntimeException if there is no active transaction
   *@throws FatalException if a SQL exception occurs during the rollback process
   */

  @Override
  public void rollBackTransaction() {
    try {
      Connection connex = null;
      if ((connex = conn.get()) == null) {
        throw new RuntimeException("Pas de transaction");
      }
      connex.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    } finally {
      fermerConnexion();
    }
  }
  /**
   *Closes the current database connection.
   */

  @Override
  public void fermerConnexion() {
    try {
      Connection connex = conn.get();
      connex.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    } finally {
      conn.remove();
    }
  }

}
