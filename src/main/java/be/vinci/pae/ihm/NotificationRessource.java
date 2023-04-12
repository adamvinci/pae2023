package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.utils.MyLogger;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
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

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<NotificationDTO> getAllNotificationsUser(@DefaultValue("-1") @PathParam("id") int id) {

    if(notificationUCC.getAllNotificationByUser(id)==null){
      return null;
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "get all notifications of user : " + id);
    return notificationUCC.getAllNotificationByUser(id);
  }

}
