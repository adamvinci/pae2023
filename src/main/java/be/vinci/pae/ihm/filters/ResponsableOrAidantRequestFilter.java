package be.vinci.pae.ihm.filters;

import be.vinci.pae.business.dto.UserDTO;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

public interface ResponsableOrAidantRequestFilter extends ContainerRequestFilter {

  /**
   * If the token is valid and the user associated with it is authorized to access the requested
   * resource.
   *
   * @param requestContext the context object representing the incoming request
   * @throws IOException if an I/O error occurs while processing the request
   */
  @Override
  void filter(ContainerRequestContext requestContext) throws IOException;

  UserDTO tokenFilter(ContainerRequestContext requestContext);
}
