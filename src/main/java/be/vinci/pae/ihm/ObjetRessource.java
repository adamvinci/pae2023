package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.business.ucc.ObjetUCC;
import be.vinci.pae.ihm.filters.PictureService;
import be.vinci.pae.ihm.filters.ResponsableAuthorization;
import be.vinci.pae.ihm.filters.ResponsableOrAidant;
import be.vinci.pae.utils.Config;
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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
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

  @Inject
  private PictureService pictureService;

  /**
   * Retrieve all the object in the database.
   *
   * @param request request the request container
   * @return a list of object or a WebAppException if there are none.
   */
  @ResponsableOrAidant
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjetDTO> getAllObject(@Context ContainerRequest request) {

    if (objetUCC.getAllObject() == null) {
      throw new WebApplicationException("No object in the database", Status.NO_CONTENT);
    }
    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");

    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO,
        "Retrieve the complete list of object from user " + authenticatedUser.getEmail());
    return objetUCC.getAllObject();

  }

  /**
   * Retrieve the object located in the store.
   *
   * @return a list containing these objects
   */
  @GET
  @Path("storeObject")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjetDTO> getObjectFromStore() {
    if (objetUCC.getAllObject() == null) {
      throw new WebApplicationException("No object in the database", Status.NO_CONTENT);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Retrieve list of object located in store  ");
    return objetUCC.getAllObject().stream()
        .filter(objetDTO -> objetDTO.getLocalisation() != null && objetDTO.getLocalisation()
            .equals("Magasin") && objetDTO.getDate_retrait() == null).toList();
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
    if (objetUCC.getAllObjectType() == null) {
      throw new WebApplicationException("No type of object in the database", Status.NO_CONTENT);
    }
    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Retrieve the type of object");
    return objetUCC.getAllObjectType();
  }

  /**
   *Retrieves a single instance of TypeObjetDTO with the specified id from the server.
   *
   *@param id the id of the TypeObjetDTO instance to retrieve
   *@return a TypeObjetDTO object in JSON format
   *@throws WebApplicationException if the id is -1 or if the requested TypeObjetDTO instance does
   *        not exist on the server
   */
  @GET
  @Path("/typeObjet/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public TypeObjetDTO getOneType(@PathParam("id") int id) {
    if (id == -1) {
      throw new WebApplicationException("Id of type required", Status.BAD_REQUEST);
    }
    TypeObjetDTO typeObjetDTO = objetUCC.getOneType(id);
    if (typeObjetDTO == null) {
      throw new WebApplicationException("type invalide",
          Status.NOT_FOUND);
    }
    return typeObjetDTO;
  }

  /**
   *Adds the provided object to the system and returns the added object with
   * its ID and photo path set.
   *
   *@param objet the object to add to the system
   *@return the added object with its ID and photo path set
   *@throws WebApplicationException if any required fields are missing or empty
   */
  @POST
  @Path("ajouterObjet")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjetDTO ajouterObjet(ObjetDTO objet) {
    System.out.println("dezdzdz");
    if (objet.getDescription().isBlank() || objet.getDescription().isEmpty()
        || objet.getPhoto().isBlank() || objet.getPhoto().isEmpty()) {
        throw new WebApplicationException("missing fields", Status.BAD_REQUEST);
    }
    objet.setPhoto(Config.getProperty("pathToObjectImage") + objet.getPhoto());

    objet = objetUCC.ajouterObjet(objet);

    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "ajout de l'objet : "
        + objet.getDescription());

    return objet;

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
      throw new WebApplicationException("This object does not exist", Status.NOT_FOUND);
    }
    NotificationDTO notification = notificationFactory.getNotification();
    ObjetDTO changedObject = objetUCC.accepterObjet(retrievedObject, notification);
    if (changedObject == null) {
      throw new WebApplicationException("Impossible changement, to accept "
          + "an object it state must be 'proposer' ", 412);
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
      throw new WebApplicationException("localisation required", Status.BAD_REQUEST);
    }
    String localisation = json.get("localisation").asText();

    if (localisation.isBlank() || localisation.isEmpty()) {
      throw new WebApplicationException("localisation required", Status.BAD_REQUEST);
    }
    if (!localisation.equals("Magasin") && !localisation.equals("Atelier")) {
      throw new WebApplicationException(
          "An object can be deposited either in 'Atelier' or 'Magasin '"
              + "", Status.BAD_REQUEST);
    }
    ObjetDTO retrievedObject = objetUCC.getOne(id);
    if (retrievedObject == null) {
      throw new WebApplicationException("This object does not exist", Status.NOT_FOUND);
    }
    if (retrievedObject.getLocalisation() != null && retrievedObject.getLocalisation()
        .equals("Magasin") || retrievedObject.getLocalisation() != null
        && retrievedObject.getLocalisation().equals("Atelier") && localisation.equals("Atelier")) {
      throw new WebApplicationException("This object already has a location", Status.BAD_REQUEST);
    }
    retrievedObject.setLocalisation(localisation);
    ObjetDTO changedObject = objetUCC.depotObject(retrievedObject);
    if (changedObject == null) {
      throw new WebApplicationException(
          "Impossible changement, to deposite an object it state must be 'accepte'",
          Status.PRECONDITION_FAILED);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Deposit of object : " + id + " at " + localisation);
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
      throw new WebApplicationException("Price required", Status.BAD_REQUEST);
    }
    String prix = json.get("prix").asText();
    if (prix.isBlank() || prix.isEmpty()) {
      throw new WebApplicationException("Price required", Status.BAD_REQUEST);
    }
    ObjetDTO retrievedObject = objetUCC.getOne(id);
    if (retrievedObject == null) {
      throw new WebApplicationException("This object does not exist", Status.NOT_FOUND);
    }

    if (Double.parseDouble(prix) > 10) {
      throw new WebApplicationException("The price must be inferior to 10", 412);
    }
    retrievedObject.setPrix(Double.parseDouble(prix));
    ObjetDTO changedObject = objetUCC.mettreEnVente(retrievedObject);

    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Put to sale of the object : " + id + " at price " + prix);
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
      throw new WebApplicationException("This object does not exist", Status.NOT_FOUND);
    }
    ObjetDTO changedObject = objetUCC.vendreObject(retrievedObject);
    if (changedObject == null) {
      throw new WebApplicationException(
          "Impossible changement,the object need to be in the state 'en vente'"
              + "to be sold", 412);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Sale of object : " + id);
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
      throw new WebApplicationException("Message required", Status.BAD_REQUEST);
    }
    String message = json.get("message").asText();

    if (message.isBlank() || message.isEmpty()) {
      throw new WebApplicationException("Message required", Status.BAD_REQUEST);
    }
    ObjetDTO retrievedObject = objetUCC.getOne(id);
    if (retrievedObject == null) {
      throw new WebApplicationException("This object does not exist", Status.NOT_FOUND);
    }
    NotificationDTO notification = notificationFactory.getNotification();
    ObjetDTO changedObject = objetUCC.refuserObject(retrievedObject, message, notification);
    if (changedObject == null) {
      throw new WebApplicationException("\"Impossible changement, to refuse an object "
          + "it state must be 'proposer'", 412);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Refusal of objet : " + id);
    return changedObject;
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
  public Response getPictureObject(@DefaultValue("-1") @PathParam("id") int id) {
    if (id == -1) {
      throw new WebApplicationException("Id of photo required", Status.BAD_REQUEST);
    }
    String pathPicture = objetUCC.getPicture(id);
    if (pathPicture == null) {
      throw new WebApplicationException("No image for this object in the database",
          Status.NOT_FOUND);
    }

    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Retrieve picture of object " + id);
    return pictureService.transformImage(pathPicture);
  }

  /**
   * Upload the photo of an object.
   *
   * @param file            the image
   * @param fileDisposition the file data as a FormDataContentDisposition object, containing the
   *                        file name and metadata
   * @return a Response object indicating success or failure of the file upload
   */
  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) {
    String fileName = fileDisposition.getFileName();
    try {
      Files.copy(file, Paths.get(Config.getProperty("pathToObjectImage") + fileName));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Adding picture of object ");
    return Response.ok().build();
  }


}
