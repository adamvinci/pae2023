package be.vinci.pae.ihm;

import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.ihm.filters.Authorize;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Json;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.Date;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * AuthRessource retrieve the request  process by Grizzly and treat it.
 */
@Singleton
@Path("/users")
public class UserRessource {
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Inject
    private UserUcc userUcc;

    /**
     * Get all the users
     *
     *
     * @return a json object with a token(formed by the user id) the user id and the user email
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectNode getAllUsers() {
        return null;
    }
}
