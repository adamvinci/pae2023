package be.vinci.pae.ihm;

import be.vinci.pae.business.domaine.Notification;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.factory.NotificationFactoryImpl;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.business.ucc.ObjetUCC;
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
   * @return a list of object or a WebAppException if there are none.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ObjetDTO> getAllObject() {
    if (objetUCC.getAllObject() == null) {
      throw new WebApplicationException("Liste vide", Status.NO_CONTENT);
    }
    return objetUCC.getAllObject();
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
    System.out.println(pathPicture);
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
    boolean changed = objetUCC.updateObjectState(etat, objet);
    boolean added = notificationUCC.createOne();
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




}
