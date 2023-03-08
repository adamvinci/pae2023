package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.Objet;
import be.vinci.pae.business.dto.ObjetDTO;

public class ObjetFactoryImpl implements ObjetFactory {

  @Override
  public ObjetDTO getObjet() {
    return new Objet();
  }

}
