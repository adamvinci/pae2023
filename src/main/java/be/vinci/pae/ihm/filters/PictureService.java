package be.vinci.pae.ihm.filters;

import jakarta.ws.rs.core.Response;

/**
 * Used to retrieve an image from the local file.
 */
public interface PictureService {

  /**
   * Retrieve the image from the project file and send it.
   *
   * @param pathPicture to search in the file
   * @return the picture
   */
  Response transformImage(String pathPicture);
}
