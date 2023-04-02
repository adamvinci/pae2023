package be.vinci.pae.business.ucc;

import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.util.List;

public class NotificationUCC {
  @Inject
  private NotificationDAO dataServiceNotification;
  @Inject
  private DALTransaction dal;

}
