package be.vinci.pae.ihm;

import be.vinci.pae.business.domaine.Notification;
import be.vinci.pae.business.domaine.Objet;
import be.vinci.pae.business.domaine.ObjetImpl;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.business.ucc.ObjetUCC;
import com.fasterxml.jackson.databind.JsonNode;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.ihm.filters.AnonymousOrAuthorize;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
import javax.json.Json;
import javax.json.JsonObject;
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
  private NotificationUCC notificationUCC;


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

  @Path("etat")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response changeEtat(JsonNode objetCredentials) {
    if (!objetCredentials.hasNonNull("etat") || !objetCredentials.hasNonNull("id_objet")) {
      throw new WebApplicationException("etat or objet required", Status.BAD_REQUEST);
    }
    String etat = objetCredentials.get("etat").asText();

    JsonNode idObjetNode = objetCredentials.get("id_objet");
    if (idObjetNode.isNull() || !idObjetNode.isNumber()) {
      throw new WebApplicationException("id_objet is not a number", Status.BAD_REQUEST);
    }
    int objet = idObjetNode.asInt();

    if (etat.isBlank() || etat.isEmpty()) {
      throw new WebApplicationException("etat is required", Status.BAD_REQUEST);
    }
    String message;
    if (objetCredentials.hasNonNull("message")){
      message = objetCredentials.get("message").asText();
      if (message.isBlank() || message.isEmpty()){
        throw new WebApplicationException("message is required", Status.BAD_REQUEST);
      }

    }else{
      message="l'objet : "+objet+" a été refusé";
    }
    ObjetDTO obj=new ObjetImpl();
    boolean changed = objetUCC.accepterObjet(obj);
    if (!changed) {
      throw new WebApplicationException("bad credentials", Status.UNAUTHORIZED);
    }

    // Construire la réponse JSON
    JsonObject jsonObject = Json.createObjectBuilder()
        .add("message", "L'état de l'objet a été modifié avec succès")
        .build();

    // Construire la réponse JAX-RS avec un code de statut 200 OK
    return Response.ok(jsonObject).build();
  }

  //change Etat of objects
  @PUT
  @Path("accepterObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response accepterObject(@PathParam("id") int id, Objet objet) {
    if(!objet.accepterObjet()) throw new WebApplicationException("can not be accepted", Status.UNAUTHORIZED);

    ObjetDTO obj=objet;

    boolean changed = objetUCC.accepterObjet(obj);
    if (!changed) {
      throw new WebApplicationException("bad credentials", Status.UNAUTHORIZED);
    }
    // Retourne une réponse HTTP 200 OK avec un message de confirmation
    return Response.status(Response.Status.OK)
            .entity("Object with ID " + id + " accepted successfully")
            .build();
  }
  @PUT
  @Path("atelierObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response atelierObject(@PathParam("id") int id, Objet objet) {
    if(!objet.depotObject()) throw new WebApplicationException("can not be at state atelier", Status.UNAUTHORIZED);
    ObjetDTO obj=objet;
    boolean changed = objetUCC.accepterObjet(obj);
    if (!changed) {
      throw new WebApplicationException("bad credentials", Status.UNAUTHORIZED);
    }
    // Retourne une réponse HTTP 200 OK avec un message de confirmation
    return Response.status(Response.Status.OK)
            .entity("Object with ID " + id + " accepted successfully")
            .build();
  }

  @PUT
  @Path("misEnVenteObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response misEnVenteObject(@PathParam("id") int id, Objet objet) {
    if(!objet.venteObject()) throw new WebApplicationException("can not be in store", Status.UNAUTHORIZED);
    ObjetDTO obj=objet;
    boolean changed = objetUCC.venteObject(obj);
    if (!changed) {
      throw new WebApplicationException("bad credentials", Status.UNAUTHORIZED);
    }
    // Retourne une réponse HTTP 200 OK avec un message de confirmation
    return Response.status(Response.Status.OK)
            .entity("Object with ID " + id + " accepted successfully")
            .build();
  }

  @PUT
  @Path("vendreObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response vendreObject(@PathParam("id") int id, Objet objet) {
    if(!objet.venduObject()) throw new WebApplicationException("can not be sold", Status.UNAUTHORIZED);
    ObjetDTO obj=objet;
    boolean changed = objetUCC.venduObject(obj);
    if (!changed) {
      throw new WebApplicationException("bad credentials", Status.UNAUTHORIZED);
    }
    // Retourne une réponse HTTP 200 OK avec un message de confirmation
    return Response.status(Response.Status.OK)
            .entity("Object with ID " + id + " accepted successfully")
            .build();
  }

  @POST
  @Path("refuserObject/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response refuserObject(@PathParam("id") int id, JsonNode objetCredentials) {

    if (!objetCredentials.hasNonNull("message")) {
      throw new WebApplicationException("etat or objet required", Status.BAD_REQUEST);
    }


    // Retourne une réponse HTTP 200 OK avec un message de confirmation
    return Response.status(Response.Status.OK)
            .entity("Object with ID " + id + " accepted successfully")
            .build();
  }
}
