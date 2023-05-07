package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.ucc.TypeObjetUcc;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TypeObjetRessource retrieve the request process by Grizzly and treat it.
 */
@Singleton
@Path("/typeObjet")
public class TypeObjetRessource {
  @Inject
  private TypeObjetUcc typeObjetUcc;

  /**
   * Retrieve all the type of object in the database.
   *
   * @return a list of object type or a WebAppException if there are none.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<TypeObjetDTO> getAllObjectType() {
    if (typeObjetUcc.getAllObjectType() == null) {
      throw new WebApplicationException("No type of object in the database", Status.NO_CONTENT);
    }
    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO, "Retrieve the type of object");
    return typeObjetUcc.getAllObjectType();
  }

  /**
   * Retrieves a single instance of TypeObjetDTO with the specified id from the server.
   *
   * @param id the id of the TypeObjetDTO instance to retrieve
   * @return a TypeObjetDTO object in JSON format
   * @throws WebApplicationException if the id is -1 or if the requested TypeObjetDTO instance does
   *                                 not exist on the server
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public TypeObjetDTO getOneType(@PathParam("id") int id) {
    if (id == -1) {
      throw new WebApplicationException("Id of type required", Status.BAD_REQUEST);
    }
    TypeObjetDTO typeObjetDTO = typeObjetUcc.getOneType(id);
    if (typeObjetDTO == null) {
      throw new WebApplicationException("type invalide",
          Status.NOT_FOUND);
    }
    return typeObjetDTO;
  }
}
