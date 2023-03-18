package be.vinci.pae.utils;


import be.vinci.pae.business.domaine.NotificationImpl;
import be.vinci.pae.business.factory.*;
import be.vinci.pae.business.ucc.*;
import be.vinci.pae.dal.*;
import be.vinci.pae.dal.services.DALService;
import be.vinci.pae.dal.services.DALServiceImpl;
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
    bind(ObjetFactoryImpl.class).to(ObjetFactory.class).in(Singleton.class);
    bind(TypeObjetFactoryImpl.class).to(TypeObjetFactory.class).in(Singleton.class);
    bind(DisponibiliteFactoryImpl.class).to(DisponibiliteFactory.class).in(Singleton.class);
    bind(NotificationFactoryImpl.class).to(NotificationFactory.class).in(Singleton.class);

    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(TypeObjetDAOImpl.class).to(TypeObjetDAO.class).in(Singleton.class);
    bind(DisponibiliteDAOImpl.class).to(DisponibiliteDAO.class).in(Singleton.class);
    bind(ObjectDAOImpl.class).to(ObjectDAO.class).in(Singleton.class);
    bind(NotificationDAOImpl.class).to(NotificationDAO.class).in(Singleton.class);

    bind(UserUccImpl.class).to(UserUcc.class).in(Singleton.class);
    bind(ObjetUCCImpl.class).to(ObjetUCC.class).in(Singleton.class);
    bind(DisponibiliteUCCImpl.class).to(DisponibiliteUCC.class).in(Singleton.class);

    bind(DALServiceImpl.class).to(DALService.class).in(Singleton.class);

  }
}