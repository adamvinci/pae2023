package be.vinci.pae.business.factory;

import be.vinci.pae.business.dto.NotificationDTO;

/**
 * Provide an instance of {@link NotificationDTO}.
 */
public interface NotificationFactory {

  /**
   * Provide an instance of {@link NotificationDTO}.
   *
   * @return a new {@link NotificationDTO}
   */
  NotificationDTO getNotification();
}
