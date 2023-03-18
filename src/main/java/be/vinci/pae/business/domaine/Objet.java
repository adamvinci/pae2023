package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.ObjetDTO;

/**
 * Class with the business method of object.
 */
public interface Objet extends ObjetDTO {

    Boolean accepterObjet();

    Boolean refuserObjet();

    Boolean depotObject();

    Boolean venteObject();

    Boolean venduObject();
}
