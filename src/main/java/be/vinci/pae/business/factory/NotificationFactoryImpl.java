package be.vinci.pae.business.factory;

import be.vinci.pae.business.domaine.NotificationImpl;
import be.vinci.pae.business.dto.NotificationDTO;

public class NotificationFactoryImpl implements NotificationFactory {

  @Override
  public NotificationDTO getNotification() {
    return new NotificationImpl();
  }

}
