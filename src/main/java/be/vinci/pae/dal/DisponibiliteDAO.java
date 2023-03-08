package be.vinci.pae.dal;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import java.util.List;

public interface DisponibiliteDAO {

  DisponibiliteDTO getOne(int id);

  List<DisponibiliteDTO> getAll();
}
