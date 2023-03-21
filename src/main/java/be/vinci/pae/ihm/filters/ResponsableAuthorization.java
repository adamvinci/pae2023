package be.vinci.pae.ihm.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ResponsableAuthorization is an annotation called by the IHM class cheking if the user is
 * connected and has the role "responsable".
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponsableAuthorization {

}

