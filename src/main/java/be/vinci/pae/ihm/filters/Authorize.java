package be.vinci.pae.ihm.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Authorize is an annotation called by  the IHM class.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

}

