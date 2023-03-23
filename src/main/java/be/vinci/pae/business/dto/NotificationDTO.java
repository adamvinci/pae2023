package be.vinci.pae.business.dto;

import be.vinci.pae.business.domaine.NotificationImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The notification linked to an object.
 */
@JsonDeserialize(as = NotificationImpl.class)
public interface NotificationDTO {

  /**
   * Returns the ID of this notification.
   *
   * @return The ID of this notification.
   */
  Integer getId();

  /**
   * Sets the ID of this notification.
   *
   * @param id The new ID of this notification.
   */
  void setId(Integer id);

  /**
   * Returns the object associated with this notification.
   *
   * @return The object associated with this notification.
   */
  int getObject();

  /**
   * Sets the object associated with this notification.
   *
   * @param object The new object associated with this notification.
   */
  void setObject(int object);

  /**
   * Returns the message associated with this notification.
   *
   * @return The message associated with this notification.
   */
  String getMessage();

  /**
   * Sets the message associated with this notification.
   *
   * @param message The new message associated with this notification.
   */
  void setMessage(String message);

  /**
   * Returns the type of this notification.
   *
   * @return The type of this notification.
   */
  String getType();

  /**
   * Sets the type of this notification.
   *
   * @param type The new type of this notification.
   */
  void setType(String type);
}
