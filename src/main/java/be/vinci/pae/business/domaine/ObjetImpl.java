package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Implementation of Object class.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjetImpl implements Objet {


  private static final String[] POSSIBLE_LOCALISATION = {"Magasin", "Atelier"};

  private static final String[] POSSIBLE_ETAT = {"accepte", "refuser", "proposer", "vendu",
      "en vente", "retirer"};

  private static final double PRIX_MAX = 10;

  private int idObjet;

  private UserDTO utilisateur;

  private String gsm;

  private String photo;

  private TypeObjetDTO typeObjet;

  private String description;


  private DisponibiliteDTO disponibilite;

  private String etat;

  private LocalDate dateAcceptation;

  private LocalDate dateDepot;

  private LocalDate dateRetrait;

  private LocalDate dateVente;

  private Double prix;

  private String localisation;

  @Override
  public int getIdObjet() {
    return idObjet;
  }

  @Override
  public void setIdObjet(int idObjet) {
    this.idObjet = idObjet;
  }

  @Override
  public UserDTO getUtilisateur() {
    return utilisateur;
  }

  @Override
  public void setUtilisateur(UserDTO utilisateur) {
    this.utilisateur = utilisateur;
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
  public String getPhoto() {
    return photo;
  }

  @Override
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  @Override
  public TypeObjetDTO getTypeObjet() {
    return typeObjet;
  }

  @Override
  public void setTypeObjet(TypeObjetDTO typeObjet) {
    this.typeObjet = typeObjet;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public DisponibiliteDTO getDisponibilite() {
    return disponibilite;
  }

  @Override
  public void setDisponibilite(DisponibiliteDTO disponibilite) {
    this.disponibilite = disponibilite;
  }

  @Override
  public String getEtat() {
    return etat;
  }

  @Override
  public void setEtat(String etat) {
    this.etat = Arrays.stream(POSSIBLE_ETAT).filter(s -> s.equals(etat)).findFirst().orElse(null);
  }

  @Override
  public LocalDate getDate_acceptation() {
    return dateAcceptation;
  }

  @Override
  public void setDate_acceptation(LocalDate date_acceptation) {
    this.dateAcceptation = date_acceptation;
  }

  @Override
  public LocalDate getDate_depot() {
    return dateDepot;
  }

  @Override
  public void setDate_depot(LocalDate date_depot) {
    this.dateDepot = date_depot;
  }

  @Override
  public LocalDate getDate_retrait() {
    return dateRetrait;
  }

  @Override
  public void setDate_retrait(LocalDate date_retrait) {
    this.dateRetrait = date_retrait;
  }

  @Override
  public LocalDate getDate_vente() {
    return dateVente;
  }

  @Override
  public void setDate_vente(LocalDate date_vente) {
    this.dateVente = date_vente;
  }

  @Override
  public Double getPrix() {
    return prix;
  }

  @Override
  public void setPrix(Double prix) {
    this.prix = null;
    if (prix <= PRIX_MAX) {
      this.prix = prix;
    }

  }

  @Override
  public String getLocalisation() {
    return localisation;
  }

  @Override
  public void setLocalisation(String localisation) {
    this.localisation = Arrays.stream(POSSIBLE_LOCALISATION).filter(s -> s.equals(localisation))
        .findFirst().orElse(null);
  }
}
