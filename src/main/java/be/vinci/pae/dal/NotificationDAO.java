package be.vinci.pae.dal;

import be.vinci.pae.business.dto.NotificationDTO;

/**
 * NotificationDAO purpose is to communicate with the notification table in the database
 */
public interface NotificationDAO {

  NotificationDTO createOne(NotificationDTO notification);

  void linkNotifToUser(int idNotif, int idUser);
}
