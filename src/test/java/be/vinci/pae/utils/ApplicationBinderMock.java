package be.vinci.pae.utils;

import be.vinci.pae.business.factory.UserFactory;
import be.vinci.pae.dal.services.DALServiceImpl;
import be.vinci.pae.business.factory.UserFactoryImpl;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.business.ucc.UserUccImpl;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.dal.UserDAOImpl;
import be.vinci.pae.dal.services.DALTransaction;
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
    //to avoid calling the UserDataServiceImpl which call the database
    // but cant ass dev.propreties not initialzied
    bind(Mockito.mock(UserDAOImpl.class)).to(UserDAO.class);
    bind(UserFactoryImpl.class).to(UserFactory.class).in(Singleton.class);
    bind(Mockito.mock(DALServiceImpl.class)).to(DALTransaction.class);
  }
}
