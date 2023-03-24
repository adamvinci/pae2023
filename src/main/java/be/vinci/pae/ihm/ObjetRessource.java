package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.business.ucc.ObjetUCC;
import be.vinci.pae.ihm.filters.AnonymousOrAuthorize;
import be.vinci.pae.ihm.filters.ResponsableAuthorization;
import be.vinci.pae.ihm.filters.ResponsableOrAidant;
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
 * ObjetRessource retrieve the request process by Grizzly and treat it.
 */
@Singleton
@Path("/objet")
public class ObjetRessource {

  @Inject
  private ObjetUCC objetUCC;
  @Inject
  private NotificationFactory notificationFactory;

  /**
   * Retrieve all the object in the database.
   *
   * @param request request the request container
   * @return a list of object or a WebAppException if there are none.
   */
  @AnonymousOrAuthorize
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjetDTO> getAllObject(@Context ContainerRequest request) {

    if (objetUCC.getAllObject() == null) {
      throw new WebApplicationException("Liste vide", Status.NO_CONTENT);
    }
    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    if (authenticatedUser != null && (authenticatedUser.getRole().equals("responsable")
        || authenticatedUser.getRole().equals("aidant"))) {
      Logger.getLogger(MyLogger.class.getName()).log(Level.INFO,
          "Retrieve the complete list of object from user " + authenticatedUser.getEmail());
      return objetUCC.getAllObject();
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Retrieve list of object located in store from anonymous");
    return objetUCC.getAllObject().stream()
        .filter(objetDTO -> objetDTO.getLocalisation().equals("Magasin")).toList();
  }

  /**
   * Retrieve all the type of object in the database.
   *
   * @return a list of object type or a WebAppException if there are none.
   */
  @GET
  @Path("typeObjet")
  @Produces(MediaType.APPLICATION_JSON)
  public List<TypeObjetDTO> getAllObjectType() {
    if (objetUCC.getAllObject() == null) {
      throw new WebApplicationException("Liste vide", Status.NO_CONTENT);
    }
    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Retrieve the type of object");
    return objetUCC.getAllObjectType();
  }

  /**
   * Return the picture linked to an object id.
   *
   * @param id to search
   * @return the path of the picture or an exception
   */
  @GET
  @Path("getPicture/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({"image/png", "image/jpg", "image/jpeg"})
  public Response getPicture(@DefaultValue("-1") @PathParam("id") int id) {
    if (id == -1) {
      throw new WebApplicationException("No content", Status.BAD_REQUEST);
    }
    String pathPicture = objetUCC.getPicture(id);
    if (pathPicture == null) {
      throw new WebApplicationException("No image for this object in the database",
          Status.NOT_FOUND);
      // delete from img if exists
    }

    if (!Files.exists(java.nio.file.Path.of(pathPicture))) {
      throw new WebApplicationException("Not Found in the server", Status.NOT_FOUND);
      // delete path in DB
    }
    File file = new File(pathPicture);
    StreamingOutput output = outputStream -> {
      try (FileInputStream input = new FileInputStream(file)) {
        int read;
        byte[] bytes = new byte[1024];
        while ((read = input.read(bytes)) != -1) {
          outputStream.write(bytes, 0, read);
        }
      }
    };
    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Retrieve picture of object " + id);
    return Response.ok(output).build();
  }

  /**
   * Change the state of an object from 'proposer' to 'accepte'.
   *
   * @param id of the object to change
   * @return the changed object
   */
  @ResponsableAuthorization
  @POST
  @Path("accepterObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjetDTO accepterObject(@PathParam("id") int id) {
    ObjetDTO retrievedObject = objetUCC.getOne(id);
    if (retrievedObject == null) {
      throw new WebApplicationException("this object does not exist", Status.BAD_REQUEST);
    }
    NotificationDTO notification = notificationFactory.getNotification();
    ObjetDTO changedObject = objetUCC.accepterObjet(retrievedObject, notification);
    if (changedObject == null) {
      throw new WebApplicationException("Impossible changement, to accept "
          + "an object it state must be 'proposer' ", 512);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Acceptation of object : " + id);

    return changedObject;
  }

  /**
   * Change the localisation of an object.
   *
   * @param id   of the object to modify
   * @param json contains the localisation
   * @return the modified object
   */
  @ResponsableOrAidant
  @POST
  @Path("depositObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjetDTO deposerObject(@PathParam("id") int id, JsonNode json) {
    if (!json.hasNonNull("localisation")) {
      throw new WebApplicationException("message required", Status.BAD_REQUEST);
    }
    String localisation = json.get("localisation").asText();

    if (localisation.isBlank() || localisation.isEmpty()) {
      throw new WebApplicationException("message required", Status.BAD_REQUEST);
    }
    if (!localisation.equals("Magasin") && !localisation.equals("Atelier")) {
      throw new WebApplicationException(
          "An object can be deposited either in 'Atelier' or 'Magasin '"
              + "", Status.BAD_REQUEST);
    }
    ObjetDTO retrievedObject = objetUCC.getOne(id);
    if (retrievedObject == null) {
      throw new WebApplicationException("this object does not exist", Status.BAD_REQUEST);
    }

    ObjetDTO changedObject = objetUCC.depotObject(retrievedObject, localisation);
    if (changedObject == null) {
      throw new WebApplicationException(
          "Impossible changement, to deposite an object it state must be 'accepte'"
              + "and must not have already a location except if the deposit is for an atelier",
          Status.UNAUTHORIZED);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Deposit of object : " + id + " at "+localisation);
    return changedObject;
  }

  /**
   * Change the state of an object to 'en vente'.
   *
   * @param id   of the object to modify
   * @param json contains the price of sell
   * @return the modified object
   */
  @ResponsableOrAidant
  @POST
  @Path("misEnVenteObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjetDTO misEnVenteObject(@PathParam("id") int id, JsonNode json) {
    if (!json.hasNonNull("prix")) {
      throw new WebApplicationException("price required", Status.BAD_REQUEST);
    }
    String prix = json.get("prix").asText();
    if (prix.isBlank() || prix.isEmpty()) {
      throw new WebApplicationException("price required", Status.BAD_REQUEST);
    }
    ObjetDTO retrievedObject = objetUCC.getOne(id);
    if (retrievedObject == null) {
      throw new WebApplicationException("this object does not exist", Status.BAD_REQUEST);
    }

    if(Double.parseDouble(prix)>10){
      throw new WebApplicationException("The price must be inferior to 10", 512);
    }
    retrievedObject.setPrix(Double.parseDouble(prix));
    ObjetDTO changedObject = objetUCC.mettreEnVente(retrievedObject);
    if (changedObject == null) {
      throw new WebApplicationException("Impossible changement, to put an object at"
          + " sell its statut must be 'accepte' and be deposited in the store", Status.UNAUTHORIZED);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Put to sale of the object : " + id + " at price " +prix);
    return changedObject;
  }

  /**
   * Change the state of an object to 'vendu'.
   *
   * @param id of the object to modify
   * @return the modified object
   */
  @ResponsableOrAidant
  @POST
  @Path("vendreObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjetDTO vendreObject(@PathParam("id") int id) {
    ObjetDTO retrievedObject = objetUCC.getOne(id);
    if (retrievedObject == null) {
      throw new WebApplicationException("this object does not exist", Status.BAD_REQUEST);
    }
    ObjetDTO changedObject = objetUCC.vendreObject(retrievedObject);
    if (changedObject == null) {
      throw new WebApplicationException(
          "Impossible changement,the object need to be in the state 'en vente'"
              + "to be sold", Status.UNAUTHORIZED);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Sale of the object : " + id) ;
    return changedObject;
  }

  /**
   * Refuse a proposed object.
   *
   * @param id   of the object to refuse
   * @param json containing the reason of refusal
   * @return the refused object
   */
  @ResponsableAuthorization
  @POST
  @Path("refuserObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjetDTO refuserObject(@PathParam("id") int id, JsonNode json) {

    if (!json.hasNonNull("message")) {
      throw new WebApplicationException("message required", Status.BAD_REQUEST);
    }
    String message = json.get("message").asText();

    if (message.isBlank() || message.isEmpty()) {
      throw new WebApplicationException("message required", Status.BAD_REQUEST);
    }
    ObjetDTO retrievedObject = objetUCC.getOne(id);
    if (retrievedObject == null) {
      throw new WebApplicationException("this object does not exist", Status.BAD_REQUEST);
    }
    NotificationDTO notification = notificationFactory.getNotification();
    ObjetDTO changedObject = objetUCC.refuserObject(retrievedObject, message, notification);
    if (changedObject == null) {
      throw new WebApplicationException("\"Impossible changement, to refuse an object "
          + "it state must be 'proposer'", Status.BAD_REQUEST);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Refusal of objet : " + id);
    return changedObject;
  }
}
