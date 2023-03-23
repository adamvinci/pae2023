package be.vinci.pae.ihm.filters;

import be.vinci.pae.business.dto.UserDTO;
import jakarta.ws.rs.container.ContainerRequestContext;

/**
 * Receive the token from different annotation and process it.
 */
public interface TokenFilter {

  /**
   * Extracts and validates an authentication token from the Authorization header.
   * @param requestContext The ContainerRequestContext object to extract the token from
   * @return The UserDTO object representing the authenticated user
   */
  UserDTO tokenFilter(ContainerRequestContext requestContext);
}
