package be.vinci.pae.ihm.filters;

import jakarta.ws.rs.core.Response;

/**
 * Used to retrieve an image from the local file.
 */
public interface PictureService {

  Response transformImage(String pathPicture);
}
