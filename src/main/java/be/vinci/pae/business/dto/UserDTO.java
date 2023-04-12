package be.vinci.pae.business.dto;

import be.vinci.pae.business.domaine.UserImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

/**
 * UserDTO is used to create a User without accessing to the business method.
 */
@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  /**
   * Return the version of the row.
   *
   * @return the version of the row
   */
  int getVersion();

  /**
   * Set the version of the row.
   *
   * @param version the version of the row
   */
  void setVersion(int version);

  /**
   * Returns the ID of the user.
   *
   * @return the ID of the user
   */
  Integer getId();

  /**
   * Sets the ID of the user.
   *
   * @param id the ID to set
   */
  void setId(Integer id);

  /**
   * Returns the password of the user.
   *
   * @return the password of the user
   */
  String getPassword();

  /**
   * Sets the password of the user.
   *
   * @param password the password to set
   */
  void setPassword(String password);

  /**
   * Returns the first name of the user.
   *
   * @return the first name of the user
   */
  String getNom();

  /**
   * Sets the first name of the user.
   *
   * @param nom the first name to set
   */
  void setNom(String nom);

  /**
   * Returns the last name of the user.
   *
   * @return the last name of the user
   */
  String getPrenom();

  /**
   * Sets the last name of the user.
   *
   * @param prenom the last name to set
   */
  void setPrenom(String prenom);

  /**
   * Returns the email of the user.
   *
   * @return the email of the user
   */
  String getEmail();

  /**
   * Sets the email of the user.
   *
   * @param email the email to set
   */
  void setEmail(String email);

  /**
   * Returns the image of the user.
   *
   * @return the image of the user
   */
  String getImage();

  /**
   * Sets the image of the user.
   *
   * @param image the image to set
   */
  void setImage(String image);

  /**
   * Returns the date of inscription of the user.
   *
   * @return the date of inscription of the user
   */
  LocalDate getDateInscription();

  /**
   * Sets the date of inscription of the user.
   *
   * @param dateInscription the date of inscription to set
   */
  void setDateInscription(LocalDate dateInscription);

  /**
   * Returns the role of the user.
   *
   * @return the role of the user
   */
  String getRole();

  /**
   * Sets the role of the user.
   *
   * @param role the role to set
   */
  void setRole(String role);

  /**
   * Returns the phone number of the user.
   *
   * @return the phone number of the user
   */
  String getGsm();

  /**
   * Sets the phone number of the user.
   *
   * @param gsm the phone number to set
   */
  void setGsm(String gsm);

}
