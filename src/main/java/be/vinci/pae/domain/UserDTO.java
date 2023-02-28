package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

/**
 * UserDTO is used to create a User without accessing to the business method.
 */
@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  Integer getId();

  void setId(Integer id);

  String getPassword();

  void setPassword(String password);

  String getNom();

  void setNom(String nom);

  String getPrenom();

  void setPrenom(String prenom);

  String getEmail();

  void setEmail(String email);

  String getImage();

  void setImage(String image);

  LocalDate getDateInscription();

  void setDateInscription(LocalDate dateInscription);

  String getRole();

  void setRole(String role);

  String getGsm();

  void setGsm(String gsm);
}
