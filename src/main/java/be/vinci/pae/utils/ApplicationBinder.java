package be.vinci.pae.utils;


import be.vinci.pae.business.factory.DisponibiliteFactory;
import be.vinci.pae.business.factory.DisponibiliteFactoryImpl;
import be.vinci.pae.business.factory.ObjetFactory;
import be.vinci.pae.business.factory.ObjetFactoryImpl;
import be.vinci.pae.business.factory.PlageHoraireFactory;
import be.vinci.pae.business.factory.PlageHoraireFactoryImpl;
import be.vinci.pae.business.factory.TypeObjetFactory;
import be.vinci.pae.business.factory.TypeObjetFactoryImpl;
import be.vinci.pae.business.factory.UserFactory;
import be.vinci.pae.business.factory.UserFactoryImpl;
import be.vinci.pae.business.ucc.DisponibiliteUCC;
import be.vinci.pae.business.ucc.DisponibiliteUCCImpl;
import be.vinci.pae.business.ucc.ObjetUCC;
import be.vinci.pae.business.ucc.ObjetUCCImpl;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.business.ucc.UserUccImpl;
import be.vinci.pae.dal.DisponibiliteDAO;
import be.vinci.pae.dal.DisponibiliteDAOImpl;
import be.vinci.pae.dal.ObjectDAO;
import be.vinci.pae.dal.ObjectDAOImpl;
import be.vinci.pae.dal.PlageHoraireDAO;
import be.vinci.pae.dal.PlageHoraireDAOImpl;
import be.vinci.pae.dal.TypeObjetDAO;
import be.vinci.pae.dal.TypeObjetDAOImpl;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.dal.UserDAOImpl;
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
    bind(PlageHoraireFactoryImpl.class).to(PlageHoraireFactory.class).in(Singleton.class);

    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(TypeObjetDAOImpl.class).to(TypeObjetDAO.class).in(Singleton.class);
    bind(PlageHoraireDAOImpl.class).to(PlageHoraireDAO.class).in(Singleton.class);
    bind(DisponibiliteDAOImpl.class).to(DisponibiliteDAO.class).in(Singleton.class);
    bind(ObjectDAOImpl.class).to(ObjectDAO.class).in(Singleton.class);

    bind(UserUccImpl.class).to(UserUcc.class).in(Singleton.class);
    bind(ObjetUCCImpl.class).to(ObjetUCC.class).in(Singleton.class);
    bind(DisponibiliteUCCImpl.class).to(DisponibiliteUCC.class).in(Singleton.class);

    bind(DALServiceImpl.class).to(DALService.class).in(Singleton.class);

  }
}