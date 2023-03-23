package be.vinci.pae.utils;

import be.vinci.pae.business.factory.DisponibiliteFactory;
import be.vinci.pae.business.factory.DisponibiliteFactoryImpl;
import be.vinci.pae.business.factory.ObjetFactory;
import be.vinci.pae.business.factory.ObjetFactoryImpl;
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
import be.vinci.pae.dal.TypeObjetDAO;
import be.vinci.pae.dal.TypeObjetDAOImpl;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.dal.UserDAOImpl;
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
    // because "this.userUcc"  is null
    bind(UserUccImpl.class).to(UserUcc.class).in(Singleton.class);
    bind(ObjetUCCImpl.class).to(ObjetUCC.class).in(Singleton.class);
    bind(DisponibiliteUCCImpl.class).to(DisponibiliteUCC.class).in(Singleton.class);

    //to avoid calling the UserDataServiceImpl which call the database
    // but cant ass dev.propreties not initialzied
    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
    bind(Mockito.mock(ObjectDAOImpl.class)).to(ObjectDAO.class);
    bind(Mockito.mock(TypeObjetDAOImpl.class)).to(TypeObjetDAO.class);
    bind(Mockito.mock(DisponibiliteDAOImpl.class)).to(DisponibiliteDAO.class);

    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(ObjetFactoryImpl.class).to(ObjetFactory.class).in(Singleton.class);
    bind(DisponibiliteFactoryImpl.class).to(DisponibiliteFactory.class).in(Singleton.class);
  }
}
