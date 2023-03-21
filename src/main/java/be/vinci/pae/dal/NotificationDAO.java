package be.vinci.pae.dal;

import be.vinci.pae.business.dto.NotificationDTO;

public interface NotificationDAO {

  NotificationDTO createOne(NotificationDTO notification);

  void linkNotifToUser(int idNotif, int idUser);
}
