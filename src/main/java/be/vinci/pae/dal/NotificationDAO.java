package be.vinci.pae.dal;

import be.vinci.pae.business.dto.NotificationDTO;
import java.util.List;

/**
 * NotificationDAO purpose is to communicate with the notification table in the database.
 */
public interface NotificationDAO {

  /**
   * Insert a notification in the database.
   *
   * @param notification to create
   * @return the created notification
   */
  NotificationDTO createOne(NotificationDTO notification);

  /**
   * Insert a row in notification_utilisateur.
   *
   * @param idNotif to link with the user
   * @param idUser to alert
   */
  void linkNotifToUser(int idNotif, int idUser);

  public List<NotificationDTO> findNotificationsByUser(int userId);
}
