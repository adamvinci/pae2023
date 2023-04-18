package be.vinci.pae.dal.services;

import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.exception.FatalException;
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
      if (conn.get() == null) {
        throw new FatalException("Pas de connexion");
      }
      statement = conn.get().prepareStatement(query);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return statement;
  }


  /**
   * Starts a new transaction by getting a new connection from the database and disabling
   * auto-commit.
   */
  @Override
  public void startTransaction() {
    try {
      if (conn.get() != null) {
        throw new FatalException("conn deja utilise");
      }
      Connection connex = db.getConnection();
      conn.set(connex);
      connex.setAutoCommit(false);
    } catch (SQLException exception) {
      throw new FatalException(exception);
    }

  }

  /**
   * Commits the current transaction to the database.
   */
  @Override
  public void commitTransaction() {
    try {
      Connection connex = null;
      if ((connex = conn.get()) == null) {
        return;
      }
      connex.commit();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      fermerConnexion();
    }
  }


  /**
   * Rolls back the current transaction associated with the connection.
   */
  @Override
  public void rollBackTransaction() {
    try {
      Connection connex = null;
      if ((connex = conn.get()) == null) {
        throw new FatalException("Pas de transaction");
      }
      connex.rollback();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      fermerConnexion();
    }
  }

  /**
   * Closes the current database connection.
   */
  public void fermerConnexion() {
    try {
      if (conn.get() == null) {
        throw new FatalException("Connexion null");
      }
      Connection connex = conn.get();
      connex.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      conn.remove();
    }
  }


}
