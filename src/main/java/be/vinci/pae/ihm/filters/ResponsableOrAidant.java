package be.vinci.pae.ihm.filters;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation called to verifiy if the token correspond to an 'aidant' or to the 'Responsable'.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponsableOrAidant {

}
