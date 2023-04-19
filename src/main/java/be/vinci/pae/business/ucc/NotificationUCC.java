package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.NotificationDTO;
import java.util.List;

/**
 * The NotificationUCC interface defines methods for managing notifications for a user.
 */
public interface NotificationUCC {

  /**
   * Retrieves all notifications for a given user.
   *
   * @param user The identifier of the user for which to retrieve notifications.
   * @return A list of NotificationDTO containing the user's notifications.
   */
  List<NotificationDTO> getAllNotificationByUser(int user);

  /**
   * Marks a notification as read for a given user.
   *
   * @param notificationDTO The notification to mark as read.
   * @param utilisateur     The identifier of the user marking the notification as read.
   * @return The updated NotificationDTO.
   */
  NotificationDTO setLueNotification(NotificationDTO notificationDTO, int utilisateur);

  /**
   * Retrieves a notification by its identifier.
   *
   * @param id The identifier of the notification to retrieve.
   * @return The NotificationDTO corresponding to the given identifier.
   */
  NotificationDTO getOne(int id);
}
