package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.MyLogger;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * NotificationRessource retrieve the request process by Grizzly and treat it.
 */
@Singleton
@Path("/notification")
public class NotificationRessource {

  @Inject
  private NotificationUCC notificationUCC;
  @Inject
  private NotificationFactory notificationFactory;

  /**
   * Retrieves all notifications for a given user.
   *
   * @param request The ContainerRequest object representing the incoming HTTP request.
   * @return A list of NotificationDTO objects representing all notifications for the authenticated
   * user. Returns null if no notifications are found for the user.
   * @throws FatalException if a SQLException occurs while accessing the database.
   */
  @Authorize
  @GET
  @Path("/userNotifications")
  @Produces(MediaType.APPLICATION_JSON)
  public List<NotificationDTO> getAllNotificationsUser(@Context ContainerRequest request) {

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    int id = authenticatedUser.getId();
    if (notificationUCC.getAllNotificationByUser(id) == null) {
      return null;
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "get all notifications of user : " + id);
    return notificationUCC.getAllNotificationByUser(id);
  }

  /**
   * Marks a notification as read for a given user.
   *
   * @param id      The identifier of the notification to mark as read.
   * @param request The ContainerRequest object representing the incoming HTTP request.
   * @return The updated NotificationDTO object after marking it as read.
   * @throws WebApplicationException with a NOT_FOUND status if the retrieved notification does not
   *                                 exist, or with a 412 status if the notification cannot be
   *                                 marked as read.
   * @throws FatalException          if a SQLException occurs while accessing the database.
   */
  @Authorize
  @POST
  @Path("/marquerRead/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public NotificationDTO markRead(@DefaultValue("-1") @PathParam("id") int id,
      @Context ContainerRequest request) {

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    int utilisateur = authenticatedUser.getId();

    NotificationDTO retrievedNotification = notificationUCC.getOne(id);
    if (retrievedNotification == null) {
      throw new WebApplicationException("This utilisateur does not exist", Status.NOT_FOUND);
    }
    NotificationDTO changedNotification = notificationUCC.setLueNotification(retrievedNotification,
        utilisateur);
    if (changedNotification == null) {
      throw new WebApplicationException("\"Impossible changement, to Lue an notification ", 412);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Mark Read the Notification : " + id + " for the user : " + utilisateur);
    return changedNotification;
  }
}
