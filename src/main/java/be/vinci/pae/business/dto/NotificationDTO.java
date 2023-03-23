package be.vinci.pae.business.dto;

import be.vinci.pae.business.domaine.NotificationImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The notification linked to an object.
 */
@JsonDeserialize(as = NotificationImpl.class)
public interface NotificationDTO {

  Integer getId();

  void setId(Integer id);

  int getObject();

  void setObject(int object);

  String getMessage();

  void setMessage(String message);

  String getType();

  void setType(String type);
}
