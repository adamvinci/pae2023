package be.vinci.pae.ihm.filters;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;

/**
 * This particular name binding annotation can be used to indicate that a method or class can be
 * accessed either anonymously or by an authorized user.
 */
@NameBinding
@Retention(RUNTIME)
public @interface AnonymousOrAuthorize {

}