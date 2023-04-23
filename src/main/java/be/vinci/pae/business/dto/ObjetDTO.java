package be.vinci.pae.business.dto;

import be.vinci.pae.business.domaine.ObjetImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

/**
 * The different Object on the website.
 */
@JsonDeserialize(as = ObjetImpl.class)
public interface ObjetDTO {

  /**
   * Returns the version of this object.
   *
   * @return the version of this object
   */
  int getNoVersion();

  /**
   * Sets the version of this object.
   *
   * @param noVersion of this object
   */
  void setNoVersion(int noVersion);

  /**
   * Returns the ID of this object.
   *
   * @return the ID of this object
   */
  int getIdObjet();

  /**
   * Sets the ID of this object.
   *
   * @param idObjet the new ID of this object
   */
  void setIdObjet(int idObjet);

  /**
   * Returns the {@link UserDTO} object representing the user associated with this object.
   *
   * @return the {@link UserDTO} object representing the user associated with this object
   */
  Integer getUtilisateur();

  /**
   * Sets the {@link UserDTO} object representing the user associated with this object.
   *
   * @param utilisateur the new {@link UserDTO} object representing the user associated with this
   *                    object
   */
  void setUtilisateur(Integer utilisateur);

  /**
   * Returns the GSM number associated with this object.
   *
   * @return the GSM number associated with this object
   */
  String getGsm();

  /**
   * Sets the GSM number associated with this object.
   *
   * @param gsm the new GSM number associated with this object
   */
  void setGsm(String gsm);

  /**
   * Returns the photo associated with this object.
   *
   * @return the photo associated with this object
   */
  String getPhoto();

  /**
   * Sets the photo associated with this object.
   *
   * @param photo the new photo associated with this object
   */
  void setPhoto(String photo);

  /**
   * Returns the {@link TypeObjetDTO} object representing the type of this object.
   *
   * @return the {@link TypeObjetDTO} object representing the type of this object
   */
  TypeObjetDTO getTypeObjet();

  /**
   * Sets the {@link TypeObjetDTO} object representing the type of this object.
   *
   * @param typeObjet the new {@link  TypeObjetDTO} object representing the type of this object
   */
  void setTypeObjet(TypeObjetDTO typeObjet);

  /**
   * Returns the description of this object.
   *
   * @return the description of this object
   */
  String getDescription();

  /**
   * Sets the description of this object.
   *
   * @param description the new description of this object
   */
  void setDescription(String description);

  /**
   * Returns the disponibilite of this object.
   *
   * @return the disponibilite of this object
   */
  DisponibiliteDTO getDisponibilite();

  /**
   * Sets the disponibilite of this object.
   *
   * @param disponibilite the new disponibilite of this object
   */
  void setDisponibilite(DisponibiliteDTO disponibilite);

  /**
   * Returns the etat of this object.
   *
   * @return the etat of this object
   */
  String getEtat();

  /**
   * Sets the etat of this object.
   *
   * @param etat the new etat of this object
   */
  void setEtat(String etat);

  /**
   * Returns the date when this object was accepted.
   *
   * @return the date when this object was accepted
   */
  LocalDate getDate_acceptation();

  /**
   * Sets the date when this object was accepted.
   *
   * @param dateAcceptation the new date when this object was accepted
   */
  void setDate_acceptation(LocalDate dateAcceptation);

  /**
   * Returns the date when this object was deposited.
   *
   * @return the date when this object was deposited
   */
  LocalDate getDate_depot();

  /**
   * Sets the date when this object was deposited.
   *
   * @param dateDepot the new date when this object was deposited
   */
  void setDate_depot(LocalDate dateDepot);

  /**
   * Returns the date when this object will be picked up.
   *
   * @return the date when this object will be picked up
   */
  LocalDate getDate_retrait();

  /**
   * Sets the date when this object will be picked up.
   *
   * @param dateRetrait the new
   */
  void setDate_retrait(LocalDate dateRetrait);

  /**
   * Returns the date of sale of the object.
   *
   * @return the date of sale of the object
   */
  LocalDate getDate_vente();

  /**
   * Sets the date of sale of the object.
   *
   * @param dateVente the date of sale to set
   */
  void setDate_vente(LocalDate dateVente);

  /**
   * Returns the price of the object.
   *
   * <p>The price is a {@link Double} value, which can be null if it is greater than 10, so that
   * the {@code JsonInclude.Include.NON_NULL} annotation doesn't return it.</p>
   *
   * @return the price of the object
   */
  Double getPrix();

  /**
   * Sets the price of the object.
   *
   * <p>The price is a {@link Double} value, which can be null if it is greater than 10, so that
   * the {@code JsonInclude.Include.NON_NULL} annotation doesn't return it.</p>
   *
   * @param prix the price to set
   */
  void setPrix(Double prix);

  /**
   * Returns the location of the object.
   *
   * @return the location of the object
   */
  String getLocalisation();

  /**
   * Sets the location of the object.
   *
   * @param localisation the location to set
   */
  void setLocalisation(String localisation);
}
