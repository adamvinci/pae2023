package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.MyLogger;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
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

  @Authorize
  @GET
  @Path("/userNotifications/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<NotificationDTO> getAllNotificationsUser(@DefaultValue("-1") @PathParam("id") int id) {

    if(notificationUCC.getAllNotificationByUser(id)==null){
      return null;
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "get all notifications of user : " + id);
    return notificationUCC.getAllNotificationByUser(id);
  }

  @Authorize
  @POST
  @Path("/marquerRead/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public NotificationDTO markRead(@DefaultValue("-1") @PathParam("id") int id,@Context ContainerRequest request){

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    int utilisateur= authenticatedUser.getId();

    NotificationDTO retrievedNotification = notificationUCC.getOne(id);
    if (retrievedNotification == null) {
      throw new WebApplicationException("This utilisateur does not exist", Status.NOT_FOUND);
    }
    NotificationDTO changedNotification = notificationUCC.setLueNotification(retrievedNotification,utilisateur);
    if (changedNotification == null) {
      throw new WebApplicationException("\"Impossible changement, to Lue an notification ", 412);
    }
    Logger.getLogger(MyLogger.class.getName())
            .log(Level.INFO, "Mark Read the Notification : " + id + " for the user : "+utilisateur);
    return changedNotification;
  }
}
