package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

/**
 * Implementation of Object class.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjetImpl implements Objet, ObjetDTO {


  private static final String[] POSSIBLE_LOCALISATION = {"Magasin", "Atelier"};

  private static final String[] POSSIBLE_ETAT = {"accepte", "refuser", "proposer", "vendu",
      "en vente", "retirer"};

  private static final double PRIX_MAX = 10;

  private int idObjet;

  private Integer utilisateur;

  private String gsm;

  private String photo;

  private TypeObjetDTO type;

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
  public Integer getUtilisateur() {
    return utilisateur;
  }

  @Override
  public void setUtilisateur(Integer utilisateur) {
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
    return type;
  }

  @Override
  public void setTypeObjet(TypeObjetDTO type) {
    this.type = type;
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
  public void setDate_acceptation(LocalDate dateAcceptation) {
    this.dateAcceptation = dateAcceptation;
  }

  @Override
  public LocalDate getDate_depot() {
    return dateDepot;
  }

  @Override
  public void setDate_depot(LocalDate dateDepot) {
    this.dateDepot = dateDepot;
  }

  @Override
  public LocalDate getDate_retrait() {
    return dateRetrait;
  }

  @Override
  public void setDate_retrait(LocalDate dateRetrait) {
    this.dateRetrait = dateRetrait;
  }

  @Override
  public LocalDate getDate_vente() {
    return dateVente;
  }

  @Override
  public void setDate_vente(LocalDate dateVente) {
    this.dateVente = dateVente;
  }

  @Override
  public Double getPrix() {
    return prix;
  }

  @Override
  public void setPrix(Double prix) {
    this.prix = prix;
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

  @Override
  public Boolean accepterObjet() {
    if (!Objects.equals(getEtat(), "proposer")) {
      return false;
    }
    setEtat(POSSIBLE_ETAT[0]);
    setDate_acceptation(LocalDate.now());
    return true;
  }

  @Override
  public Boolean refuserObjet() {
    if (!Objects.equals(getEtat(), "proposer")) {
      return false;
    }
    setEtat("refuser");
    return true;
  }

  @Override
  public Boolean deposerEnMagasin() {
    if (Objects.equals(getEtat(), "accepte") && Objects.equals(getLocalisation(), null)
        || Objects.equals(getLocalisation(), "Atelier") && Objects.equals(getEtat(), "accepte")) {
      setLocalisation("Magasin");
      setDate_depot(LocalDate.now());
      return true;
    }
    return false;
  }

  @Override
  public Boolean deposerEnAtelier() {
    if (Objects.equals(getEtat(), "accepte") && Objects.equals(getLocalisation(), null)) {
      setLocalisation("Atelier");
      return true;
    }
    return false;
  }

  @Override
  public Boolean mettreEnVente() {
    if (Objects.equals(getEtat(), "accepte") && Objects.equals(getLocalisation(),"Magasin")) {
      setEtat("en vente");
      return true;
    }
    return false;
  }

  @Override
  public Boolean vendreObjet() {
    if (!Objects.equals(getEtat(), "en vente")) {
      return false;
    }
    setEtat("vendu");
    setDate_vente(LocalDate.now());
    return true;
  }
}
