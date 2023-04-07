package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.ucc.DisponibiliteUCC;
import be.vinci.pae.utils.MyLogger;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.time.DayOfWeek;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DisponibiliteRessource retrieve the request process by Grizzly and treat it.
 */
@Singleton
@Path("/disponibilite")
public class DisponibiliteRessource {

  @Inject
  private DisponibiliteUCC disponibiliteUCC;

  /**
   * Get all the Availabilty of Mr.Riez and his 'aidant'.
   *
   * @return a list of availability or null if there are none
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<DisponibiliteDTO> getAllDisp() {
    if (disponibiliteUCC.getDisponibilite() == null) {
      throw new WebApplicationException("No disponibility", Status.NO_CONTENT);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Retrieving of disponibiltiy list");
    return disponibiliteUCC.getDisponibilite();
  }

  /**
   * Create a disponibility.
   *
   * @param disponibiliteDTO containing the date and plage to insert
   * @return the disponibility created
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public DisponibiliteDTO createOne(DisponibiliteDTO disponibiliteDTO) {
    if (disponibiliteDTO.getDate() == null) {
      throw new WebApplicationException("Date required", Status.BAD_REQUEST);
    }
    if (disponibiliteDTO.getDate().getDayOfWeek() != DayOfWeek.SATURDAY) {
      throw new WebApplicationException("The date has to be a sunday", Status.BAD_REQUEST);
    }
    if (disponibiliteDTO.getPlage() == null || disponibiliteDTO.getPlage().equals("")) {
      throw new WebApplicationException("Plage horaire required", Status.BAD_REQUEST);
    }
    if (!disponibiliteDTO.getPlage().equals("matin") && !disponibiliteDTO.getPlage()
        .equals("apres midi")) {
      throw new WebApplicationException("The plage can either be 'matin' or 'apres midi'",
          Status.BAD_REQUEST);
    }
    Logger.getLogger(MyLogger.class.getName())
        .log(Level.INFO, "Adding of a disponibility ");
    return disponibiliteUCC.createOne(disponibiliteDTO);
  }
}
