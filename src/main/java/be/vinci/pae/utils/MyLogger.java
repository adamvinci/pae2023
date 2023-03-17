package be.vinci.pae.utils;

import jakarta.inject.Singleton;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A simple logger class that writes log messages to a rotating file.
 */
@Singleton
public class MyLogger {

  private static Logger logger;
  private static FileHandler fileHandler;

  /**
   * Constructs a new MyLogger object with a rotating file handler.
   */
  public MyLogger() {
    logger = Logger.getLogger(MyLogger.class.getName());
    try {
      fileHandler = new FileHandler("logs/log%u.txt", 100000, 10, true);
      logger.addHandler(fileHandler);
      SimpleFormatter formatter = new SimpleFormatter();
      fileHandler.setFormatter(formatter);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to initialize logger", e);
    }
  }

}




