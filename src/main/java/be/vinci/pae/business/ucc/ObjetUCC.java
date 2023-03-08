package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import java.util.List;

public interface ObjetUCC {

  List<ObjetDTO> getAllObject();

  List<TypeObjetDTO> getAllObjectType();
}
