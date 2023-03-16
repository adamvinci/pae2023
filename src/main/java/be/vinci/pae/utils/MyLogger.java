package be.vinci.pae.utils;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Singleton
public class MyLogger {

  private static Logger logger;
  private static FileHandler fileHandler;

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




