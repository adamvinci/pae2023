package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.views.Views.Public;
import com.fasterxml.jackson.annotation.JsonView;

public class NotificationImpl implements Notification {

  @JsonView(Public.class)
  private Integer id;
  @JsonView(Public.class)
  private ObjetDTO object;
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

  public ObjetDTO getObject() {
    return object;
  }

  public void setObject(ObjetDTO object) {
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
