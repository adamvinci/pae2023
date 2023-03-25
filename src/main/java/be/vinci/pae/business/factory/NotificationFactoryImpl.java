package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.NotificationImpl;
import be.vinci.pae.business.dto.NotificationDTO;

/**
 * Implementation of {@link NotificationFactory}.
 */
public class NotificationFactoryImpl implements NotificationFactory {

  @Override
  public NotificationDTO getNotification() {
    return new NotificationImpl();
  }

}
