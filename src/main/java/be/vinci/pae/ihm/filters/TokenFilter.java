package be.vinci.pae.ihm.filters;

import be.vinci.pae.business.dto.UserDTO;
import jakarta.ws.rs.container.ContainerRequestContext;

public interface TokenFilter {

  UserDTO tokenFilter(ContainerRequestContext requestContext);
}
