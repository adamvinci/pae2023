package be.vinci.pae.utils;

import be.vinci.pae.domain.UserUcc;
import be.vinci.pae.domain.UserUccImpl;
import be.vinci.pae.services.UserDataService;
import be.vinci.pae.services.UserDataServiceImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

/**
 * ApplicationBinderMock.
 */
@Provider
public class ApplicationBinderMock extends AbstractBinder {


  @Override
  protected void configure() {
    // to avoid error Cannot invoke "be.vinci.pae.domain.UserUcc.getOne(int)"
    // because "this.userUcc" is null
    bind(UserUccImpl.class).to(UserUcc.class).in(Singleton.class);
    //to avoid calling the UserDataServiceImpl which call the database
    // but cant ass dev.propreties not initialzied
    bind(Mockito.mock(UserDataServiceImpl.class)).to(UserDataService.class);
  }
}
