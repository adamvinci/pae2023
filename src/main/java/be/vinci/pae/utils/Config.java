package be.vinci.pae.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config handle the .propreties file.
 */
public class Config {

  private static Properties props;

  /**
   * Charge the file which contains the real value of references.
   *
   * @param file the file
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Replace the reference with real values.
   *
   * @param key the references
   * @return the real values
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  /**
   * Retrieves the value of the specified property as an integer.
   *
   * @param key the key of the property to retrieve
   * @return the value of the property as an integer
   * @throws NumberFormatException    if the property value cannot be parsed as an integer
   * @throws NullPointerException     if the property value is null
   * @throws IllegalArgumentException if the property value is not a valid integer
   */
  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  /**
   * Retrieves the value of the specified property as a boolean.
   *
   * @param key the key of the property to retrieve
   * @return the value of the property as a boolean
   * @throws NullPointerException     if the property value is null
   * @throws IllegalArgumentException if the property value is not a valid boolean
   */
  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }

}