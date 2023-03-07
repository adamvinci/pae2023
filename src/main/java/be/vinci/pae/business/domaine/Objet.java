package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.views.Views.Internal;
import be.vinci.pae.views.Views.Public;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDate;

/**
 * Implementation of Object class.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Objet {

  @JsonView(Public.class)
  private static final String[] POSSIBLE_LOCALISATION = {"Magasin", "Atelier"};
  @JsonView(Public.class)
  private static final String[] POSSIBLE_ETAT = {"accepte", "refuser", "proposer", "vendu",
      "en vente", "retirer",};
  @JsonView(Public.class)
  private int idObjet;
  @JsonView(Internal.class)
  private UserDTO utilisateur;
  @JsonView(Internal.class)
  private String gsm;
  @JsonView(Public.class)
  private String photo;
  @JsonView(Public.class)
  private TypeObjetDTO typeObjet;
  @JsonView(Public.class)
  private String description;
  @JsonView(Internal.class)
  private LocalDate date_acceptation;
  @JsonView(Internal.class)
  private LocalDate date_depot;
  @JsonView(Internal.class)
  private LocalDate date_retrait;
  @JsonView(Internal.class)
  private LocalDate date_vente;
  @JsonView(Internal.class)
  private double prix;
  @JsonView(Internal.class)
  private String localisation;
  @JsonView(Public.class)
  private String etat;
}
