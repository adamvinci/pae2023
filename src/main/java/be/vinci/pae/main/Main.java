package be.vinci.pae.main;

import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.WebExceptionMapper;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {

  // Base URI the Grizzly HTTP server will listen on


  static {
    Config.load("dev.properties");
  }

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resource config that scans for JAX-RS resources and providers
    // in vinci.be package
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.api")
        .register(WebExceptionMapper.class)
        .register(ApplicationBinder.class);

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(Config.getProperty("BaseUri")), rc);
  }

  /**
   * Main method.
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app started with WADL available at "
        + "%sapplication.wadl\nHit enter to stop it...", Config.getProperty("BaseUri")));
    System.in.read();
    server.stop();
  }
}
