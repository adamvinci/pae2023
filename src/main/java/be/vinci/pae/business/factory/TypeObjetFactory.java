package be.vinci.pae.business.factory;

import be.vinci.pae.business.dto.TypeObjetDTO;

/**
 * Provide an instance of {@link TypeObjetDTO}.
 */
public interface TypeObjetFactory {

  /**
   * Returns a new {@link  TypeObjetDTO} object.
   *
   * @return a new {@link TypeObjetDTO} object
   */
  TypeObjetDTO getTypeObjet();
}
