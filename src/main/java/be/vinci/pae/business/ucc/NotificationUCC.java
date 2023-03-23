package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.dal.NotificationDAO;

public class NotificationUCC {

  private NotificationDAO dataService;

  public NotificationDTO createOne(NotificationDTO notificationDTO) {
    return dataService.createOne(notificationDTO);
  }
}
