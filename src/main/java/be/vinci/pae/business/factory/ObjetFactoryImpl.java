package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.ObjetImpl;
import be.vinci.pae.business.dto.ObjetDTO;

/**
 * Implementation of {@link ObjetFactory}.
 */
public class ObjetFactoryImpl implements ObjetFactory {

  /**
   * Returns a new {@link ObjetDTO} object .
   *
   * @return a new {@link ObjetDTO} object
   */
  @Override
  public ObjetDTO getObjet() {
    return new ObjetImpl();
  }

}
