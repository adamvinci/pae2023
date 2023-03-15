package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.ObjetUCC;
import be.vinci.pae.ihm.filters.AnonymousOrAuthorize;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
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
import org.glassfish.jersey.server.ContainerRequest;

/**
 * ObjetRessource retrieve the request process by Grizzly and treat it.
 */
@Singleton
@Path("/objet")
public class ObjetRessource {

  @Inject
  private ObjetUCC objetUCC;

  /**
   * Retrieve all the object in the database.
   *
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
      return objetUCC.getAllObject();
    }
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
    return Response.ok(output).build();
  }
}
