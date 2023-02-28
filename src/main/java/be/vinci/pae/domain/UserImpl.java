package be.vinci.pae.domain;

import be.vinci.pae.views.Views.Internal;
import be.vinci.pae.views.Views.Public;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDate;
import java.util.Arrays;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementation of User.
 */
@JsonInclude(Include.NON_DEFAULT)
public class UserImpl implements User {

  @JsonView(Public.class)
  private static final String[] POSSIBLE_ROLE = {"Responsable", "aidant", "membre"};
  @JsonView(Public.class)
  private Integer id;
  @JsonView(Internal.class)
  private String password;
  @JsonView(Public.class)
  private String nom;
  @JsonView(Public.class)
  private String prenom;
  @JsonView(Public.class)
  private String email;
  @JsonView(Public.class)
  private String image;
  @JsonView(Public.class)
  private LocalDate dateInscription;
  @JsonView(Public.class)
  private String role;
  @JsonView(Public.class)
  private String gsm;


  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public String getPrenom() {
    return prenom;
  }

  @Override
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getImage() {
    return image;
  }

  @Override
  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public LocalDate getDateInscription() {
    return dateInscription;
  }

  @Override
  public void setDateInscription(LocalDate dateInscription) {
    this.dateInscription = dateInscription;
  }

  @Override
  public String getRole() {
    return role;
  }

  @Override
  public void setRole(String role) {
    this.role = Arrays.stream(POSSIBLE_ROLE).filter(s -> s.equals(role)).findFirst().orElse(null);
  }

  @Override
  public String getGsm() {
    return gsm;
  }

  @Override
  public void setGsm(String gsm) {
    this.gsm = gsm;
  }

  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public boolean checkCanBeAdmin(User user) {
    return user.getRole().equals("membre");
  }

  @Override
  public boolean changeToAdmin(User user) {
    if (!checkCanBeAdmin(user)) {
      return false;
    }
    user.setRole("aidant");
    return true;
  }


}
