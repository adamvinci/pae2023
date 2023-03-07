package be.vinci.pae.business.dto;


import be.vinci.pae.business.domaine.TypeObjetImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = TypeObjetImpl.class)
public interface TypeObjetDTO {

  int getIdObjet();

  void setIdObjet(int idObjet);

  String getLibelle();

  void setLibelle(String libelle);
}
