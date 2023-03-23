package be.vinci.pae.business.factory;

import be.vinci.pae.business.dto.NotificationDTO;

/**
 * Provide an instance of {@link NotificationDTO}.
 */
public interface NotificationFactory {

  NotificationDTO getNotification();
}
