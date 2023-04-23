package be.vinci.pae.dal;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.utils.exception.BusinessException;
import java.util.List;

/**
 * NotificationDAO purpose is to communicate with the notification table in the database.
 */
public interface NotificationDAO {

  /**
   * Insert a notification in the database.
   *
   * @param notification to create
   * @return the created notification
   */
  NotificationDTO createOne(NotificationDTO notification);

  /**
   * Insert a row in notification_utilisateur.
   *
   * @param idNotif to link with the user
   * @param idUser  to alert
   */
  void linkNotifToUser(int idNotif, int idUser);

  /**
   * Recherche les notifications associées à un utilisateur donné.
   *
   * @param userId L'identifiant de l'utilisateur.
   * @return Une liste de {@link NotificationDTO} représentant les notifications associées à
   * l'utilisateur.
   * @throws BusinessException Si une exception métier se produit lors de l'exécution de la
   *                           recherche.
   */
  List<NotificationDTO> findNotificationsByUser(int userId);

  /**
   * Marque une notification comme lue pour un utilisateur donné.
   *
   * @param notificationDTO L'objet {@link NotificationDTO} représentant la notification à marquer
   *                        comme lue.
   * @param utilisateur     L'identifiant de l'utilisateur.
   * @return L'objet {@link NotificationDTO} représentant la notification mise à jour.
   * @throws BusinessException Si une exception métier se produit lors de la mise à jour de la
   *                           notification.
   */
  NotificationDTO setLueNotification(NotificationDTO notificationDTO, int utilisateur);

  /**
   * Récupère une notification par son identifiant.
   *
   * @param id L'identifiant de la notification.
   * @return L'objet {@link NotificationDTO} représentant la notification correspondant à
   * l'identifiant donné.
   * @throws BusinessException Si une exception métier se produit lors de la récupération de la
   *                           notification.
   */
  NotificationDTO getOne(int id);
}
