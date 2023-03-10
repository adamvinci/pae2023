package be.vinci.pae.business.factory;

import be.vinci.pae.business.dto.ObjetDTO;

/**
 * Provide an instance of {@link ObjetDTO}.
 */
public interface ObjetFactory {

  /**
   * Returns a new {@link ObjetDTO} object .
   *
   * @return a new {@link ObjetDTO} object
   */
  ObjetDTO getObjet();
}
