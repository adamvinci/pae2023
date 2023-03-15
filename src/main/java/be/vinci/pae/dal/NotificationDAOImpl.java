package be.vinci.pae.dal;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.dal.services.DALService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationDAOImpl implements NotificationDAO {

  private DALService dalService;
  private ObjectDAO objectDAO;
  private NotificationFactory notificationFactory;

  @Override
  public NotificationDTO createOne(NotificationDTO notification) {
    NotificationDTO notificationDTO = notificationFactory.getNotification();
    try (PreparedStatement statement = dalService.preparedStatement(
        "INSERT INTO projet.notifications VALUES "
            + "(DEFAULT,?,?,?) RETURNING *;")) {
      statement.setInt(1, notification.getObject().getIdObjet());
      statement.setString(2, notification.getMessage());
      statement.setString(3, notification.getType());
      try (ResultSet set = statement.executeQuery()) {
        if (!set.isBeforeFirst()) {
          return null;
        } else {
          while (set.next()) {
            initUser(userDTO, set.getString(2), set.getString(3), set.getString(4),
                set.getString(5), set.getString(6), set.getDate(7).toLocalDate(),
                set.getString(8),
                set.getString(9), set.getInt(1));

          }
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  public void initNotification(NotificationDTO notificationDTO, int id, ObjectDTO object,
      String message, String type) {

  }
}
