package be.vinci.pae.business.dto;

import be.vinci.pae.business.domaine.NotificationImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = NotificationImpl.class)
public interface NotificationDTO {

  public Integer getId();

  public void setId(Integer id);

  public int getObject();

  public void setObject(int object);

  public String getMessage();

  public void setMessage(String message);

  public String getType();

  public void setType(String type);
}
