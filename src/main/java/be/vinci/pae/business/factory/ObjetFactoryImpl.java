package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.ObjetImpl;
import be.vinci.pae.business.dto.ObjetDTO;

/**
 * Implementation of {@link ObjetFactory}.
 */
public class ObjetFactoryImpl implements ObjetFactory {

  @Override
  public ObjetDTO getObjet() {
    return new ObjetImpl();
  }

}
