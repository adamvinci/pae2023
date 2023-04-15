package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.NotificationDTO;
import java.util.List;

public interface NotificationUCC {

  public List<NotificationDTO> getAllNotificationByUser(int user);

  public NotificationDTO setLueNotification(NotificationDTO notificationDTO, int utilisateur);

  NotificationDTO getOne(int id);
}
