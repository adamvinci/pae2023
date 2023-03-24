package be.vinci.pae.business.domaine;

import be.vinci.pae.business.dto.NotificationDTO;
import java.util.Arrays;

/**
 * Implementation of {@link NotificationDTO}.
 */
public class NotificationImpl implements NotificationDTO {

  private int id;
  private int object;
  private String message;
  private String type;

  private static final String[] POSSIBLE_TYPE = {"acceptation", "refus", "alerteProposition"};

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
    this.type = Arrays.stream(POSSIBLE_TYPE).filter(s -> s.equals(type)).findFirst().orElse(null);
  }
}
