package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.ObjetDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Class with the business method of object.
 */
@JsonDeserialize(as = Objet.class)
public interface Objet extends ObjetDTO {

    Boolean accepterObjet();

    Boolean refuserObjet();

    Boolean depotObject();

    Boolean venteObject();

    Boolean venduObject();
}
