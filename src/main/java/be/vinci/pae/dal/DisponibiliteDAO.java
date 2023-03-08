package be.vinci.pae.dal;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import java.util.List;

/**
 * DisponibiliteDAO purpose is to communicate with the database.
 */
public interface DisponibiliteDAO {

  DisponibiliteDTO getOne(int id);

  List<DisponibiliteDTO> getAll();
}
