package be.vinci.pae.business.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

@JsonDeserialize(as = Object.class)
public interface ObjetDTO {

  int getIdObjet();

  void setIdObjet(int idObjet);

  UserDTO getUtilisateur();

  void setUtilisateur(UserDTO utilisateur);

  String getGsm();

  void setGsm(String gsm);

  String getPhoto();

  void setPhoto(String photo);

  TypeObjetDTO getTypeObjet();

  void setTypeObjet(TypeObjetDTO typeObjet);

  String getDescription();

  void setDescription(String description);

  DisponibiliteDTO getDisponibilite();

  void setDisponibilite(DisponibiliteDTO disponibilite);

  String getEtat();

  void setEtat(String etat);

  LocalDate getDate_acceptation();

  void setDate_acceptation(LocalDate date_acceptation);

  LocalDate getDate_depot();

  void setDate_depot(LocalDate date_depot);

  LocalDate getDate_retrait();

  void setDate_retrait(LocalDate date_retrait);

  LocalDate getDate_vente();

  void setDate_vente(LocalDate date_vente);


  Double getPrix();

  /**
   * the price is Double so it can be null if > 10 and so that the Json.include.nonnull dont return
   * it.
   *
   * @param prix to set
   */
  void setPrix(Double prix);

  String getLocalisation();

  void setLocalisation(String localisation);

}
