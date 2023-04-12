package be.vinci.pae.dal;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.dal.services.DALService;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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
      if (notification.getMessage() != null) {
        statement.setString(2, notification.getMessage());
      } else {
        statement.setNull(2, Types.VARCHAR);
      }

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

  /**
   * Initializes the given NotificationDTO object with the specified values for its properties.
   *
   * @param notificationDTO The NotificationDTO object to be initialized.
   * @param id              The value to set for the 'id' property of the NotificationDTO.
   * @param object          The value to set for the 'object' property of the NotificationDTO.
   * @param message         The value to set for the 'message' property of the NotificationDTO.
   * @param type            The value to set for the 'type' property of the NotificationDTO.
   */
  public void initNotification(NotificationDTO notificationDTO, int id, int object,
      String message, String type) {
    notificationDTO.setId(id);
    notificationDTO.setObject(object);
    notificationDTO.setMessage(message);
    notificationDTO.setType(type);
  }

  public List<NotificationDTO> findNotificationsByUser(int userId) {
    try (PreparedStatement statement = dalService.preparedStatement(
          "SELECT n.id_notification, n.objet, n.message, n.type, nu.lue " +
              "FROM projet.notifications n " +
              "JOIN projet.notifications_utilisateurs nu ON n.id_notification = nu.notification " +
              "WHERE nu.utilisateur_notifie = ? " +
              "ORDER BY n.id_notification DESC")){
      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();
      List<NotificationDTO> notifications = new ArrayList<>();
      while (rs.next()) {
        NotificationDTO notification = notificationFactory.getNotification();
        notification.setId(rs.getInt("id_notification"));
        notification.setObject(rs.getInt("objet"));
        notification.setMessage(rs.getString("message"));
        notification.setType(rs.getString("type"));
        notification.setLue(rs.getBoolean("lue"));
        notifications.add(notification);
      }
      return notifications;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}
