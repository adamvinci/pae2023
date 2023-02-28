package be.vinci.pae.utils;


import be.vinci.pae.business.factory.UserFactory;
import be.vinci.pae.business.factory.UserFactoryImpl;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.business.ucc.UserUccImpl;
import be.vinci.pae.dal.UserDataService;
import be.vinci.pae.dal.UserDataServiceImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * ApplicationBinderClass allow to inject service  object.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(UserDataServiceImpl.class).to(UserDataService.class).in(Singleton.class);
    bind(UserUccImpl.class).to(UserUcc.class).in(Singleton.class);

  }
}