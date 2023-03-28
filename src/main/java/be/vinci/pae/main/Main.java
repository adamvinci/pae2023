package be.vinci.pae.main;

import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.MyLogger;
import be.vinci.pae.utils.WebExceptionMapper;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Launch the webserver and initialize the .propreties file.
 */
public class Main {

  /* Base URI the Grizzly HTTP server will listen on */


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
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.ihm")
        .register(WebExceptionMapper.class)
        .register(ApplicationBinder.class)
        .register(MultiPartFeature.class);

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(Config.getProperty("BaseUri")), rc);
  }

  /**
   * This is the main method which starts the Jersey application.
   *
   * @param args an array of command-line arguments for the application
   * @throws IOException IOException if an I/O error occurs while reading input from the console
   */
  public static void main(String[] args) throws IOException {
    new MyLogger();
    final HttpServer server = startServer();
    Logger.getLogger(MyLogger.class.getName()).log(Level.INFO,String.format("Jersey app started with WADL available at "
        + "%sapplication.wadl\nHit enter to stop it...", Config.getProperty("BaseUri")));
    System.in.read();
    server.stop();
  }
}
