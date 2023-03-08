package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.ucc.DisponibiliteUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

/**
 * DisponibiliteRessource retrieve the request process by Grizzly and treat it.
 */
@Singleton
@Path("/disponibilite")
public class DisponibiliteRessource {

  @Inject
  private DisponibiliteUCC disponibiliteUCC;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<DisponibiliteDTO> getAllDisp() {
    if (disponibiliteUCC.getDisponibilite() == null) {
      throw new WebApplicationException("Liste vide", Status.NO_CONTENT);
    }
    return disponibiliteUCC.getDisponibilite();
  }
}
