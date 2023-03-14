package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.TypeObjetImpl;
import be.vinci.pae.business.dto.TypeObjetDTO;

/**
 * Implementation of {@link TypeObjetFactory}.
 */
public class TypeObjetFactoryImpl implements TypeObjetFactory {

  /**
   * Returns a new {@link  TypeObjetDTO} object.
   *
   * @return a new {@link TypeObjetDTO} object
   */
  @Override
  public TypeObjetDTO getTypeObjet() {
    return new TypeObjetImpl();
  }

}
