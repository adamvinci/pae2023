package be.vinci.pae.dal;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.dal.services.DALService;
import be.vinci.pae.utils.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of {@link NotificationDAO}.
 */
public class NotificationDAOImpl implements NotificationDAO {

  @Inject
  private DALService dalService;
  @Inject
  private NotificationFactory notificationFactory;


  @Override
  public NotificationDTO createOne(NotificationDTO notification) {
    NotificationDTO notificationDTO = notificationFactory.getNotification();
    try (PreparedStatement statement = dalService.preparedStatement(
        "INSERT INTO projet.notifications VALUES "
            + "(DEFAULT,?,?,?) RETURNING *;")) {
      statement.setInt(1, notification.getObject());
      statement.setString(2, notification.getMessage());
      statement.setString(3, notification.getType());
      try (ResultSet set = statement.executeQuery()) {
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            initNotification(notificationDTO, set.getInt(1), set.getInt(2), set.getString(3),
                set.getString(4));

          }
        }
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return notificationDTO;
  }


  @Override
  public void linkNotifToUser(int idNotif, int idUser) {
    try (PreparedStatement statement = dalService.preparedStatement(
        "INSERT INTO projet.notifications_utilisateurs (notification, utilisateur_notifie,lue)"
            + " VALUES (?,?,FALSE) RETURNING *")) {
      statement.setInt(1, idNotif);
      statement.setInt(2, idUser);
      statement.executeQuery();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  public void initNotification(NotificationDTO notificationDTO, int id, int object,
      String message, String type) {
    notificationDTO.setId(id);
    notificationDTO.setObject(object);
    notificationDTO.setMessage(message);
    notificationDTO.setType(type);
  }
}
