package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.TypeObjetDTO;

/**
 * Implements directly {@link TypeObjetDTO} as it does not need a business class with business method.
 */
public class TypeObjetImpl implements TypeObjetDTO {

  private int idObjet;
  private String libelle;

  @Override
  public int getIdObjet() {
    return idObjet;
  }

  @Override
  public void setIdObjet(int idObjet) {
    this.idObjet = idObjet;
  }

  @Override
  public String getLibelle() {
    return libelle;
  }

  @Override
  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }
}
