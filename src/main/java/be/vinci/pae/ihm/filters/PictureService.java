package be.vinci.pae.ihm.filters;

import jakarta.ws.rs.core.Response;

public interface PictureService {

  Response transformImage(String pathPicture);
}
