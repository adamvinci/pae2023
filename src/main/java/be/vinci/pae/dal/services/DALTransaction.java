package be.vinci.pae.dal.services;

import be.vinci.pae.utils.FatalException;

/**
 The DALTransaction interface provides methods for managing database transactions.
 */
public interface DALTransaction  {


  /**
   * Starts a new transaction by getting a new connection from the database and disabling
   * auto-commit.
   */
  void startTransaction();

  /**
   *Commits the current transaction to the database.

   *Throws a RuntimeException if no connection is available.
   *Any SQLException encountered during the commit will be wrapped in a FatalException and rethrown.
   *At the end, the connection is closed.
   */
  void commitTransaction();

  /**
   *Rolls back the current transaction associated with the connection.

   *@throws RuntimeException if there is no active transaction
   *@throws FatalException if a SQL exception occurs during the rollback process
   */
  void rollBackTransaction();


}
