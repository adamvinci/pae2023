package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.views.Views.Public;
import com.fasterxml.jackson.annotation.JsonView;

public class NotificationImpl implements NotificationDTO {

  @JsonView(Public.class)
  private int id;
  @JsonView(Public.class)
  private int object;
  @JsonView(Public.class)
  private String message;
  @JsonView(Public.class)
  private String type;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getObject() {
    return object;
  }

  public void setObject(int object) {
    this.object = object;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
