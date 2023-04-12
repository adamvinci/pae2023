package be.vinci.pae.business.ucc;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.util.List;

public class NotificationUCCImpl implements NotificationUCC {
  @Inject
  private NotificationDAO dataService;
  @Inject
  private DALTransaction dal;

  @Override
  public List<NotificationDTO> getAllNotificationByUser(int user) {
    try {
      dal.startTransaction();
      return dataService.findNotificationsByUser(user);
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

}
