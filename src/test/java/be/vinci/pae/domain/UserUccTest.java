package be.vinci.pae.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.domaine.UserImpl;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.utils.ApplicationBinderMock;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * UserUccTest.
 */
class UserUccTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderMock());

  private UserUcc userUcc;
  private UserDAO userDAO;
  private User userSteven;


  @BeforeEach
  void setUp() {
    userUcc = locator.getService(UserUcc.class);
    userDAO = locator.getService(UserDAO.class);
    userSteven = Mockito.mock(UserImpl.class);

    Mockito.when(userSteven.getId()).thenReturn(2);
    Mockito.when(userSteven.getEmail()).thenReturn("steven.agbassah@student.vinci.be");
    Mockito.when(userSteven.getRole()).thenReturn("aidant");
    Mockito.when(userSteven.getPassword()).thenReturn("123*");
    Mockito.when(userSteven.checkPassword("123*")).thenReturn(true);

    Mockito.when(userDAO.getOne(2)).thenReturn(userSteven);
    Mockito.when(userDAO.getOne("steven.agbassah@student.vinci.be")).thenReturn(userSteven);

  }

  @DisplayName("Test login(String email, String password) w ith good email and good password")
  @Test
  void testLoginWithGoodCredentials() {

    assertEquals(userSteven.getId(),
        userUcc.login("steven.agbassah@student.vinci.be", "123*").getId());
  }

  @DisplayName("Test login(String email, String password) with good email and bad password")
  @Test
  void testLoginWithGoodEmailAndBadPassword() {
    assertNull(userUcc.login("steven.agbassah@student.vinci.be", "123"), "Mauvais mot de passe");

  }

  @DisplayName("Test  login(String email, String password) with bad email ")
  @Test
  void testLoginWithBadEmailAndGoodPassword() {
    Mockito.when(userDAO.getOne("stevenagbassah@student.vinci.be"))
        .thenReturn(null);
    assertNull(userUcc.login("stevenagbassah@student.vinci.be", "123*"),
        "Cette email n'existe pas");

  }

  @DisplayName("Test getOne(id) with the good id")
  @Test
  void getOneGoodId() {
    assertEquals(userSteven.getEmail(), userUcc.getOne(2).getEmail());
  }


}