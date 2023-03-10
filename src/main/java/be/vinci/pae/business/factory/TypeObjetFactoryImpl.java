package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.TypeObjetImpl;
import be.vinci.pae.business.dto.TypeObjetDTO;

/**
 * Implementation of {@link TypeObjetFactory}.
 */
public class TypeObjetFactoryImpl implements TypeObjetFactory {

  @Override
  public TypeObjetDTO getTypeObjet() {
    return new TypeObjetImpl();
  }

}
