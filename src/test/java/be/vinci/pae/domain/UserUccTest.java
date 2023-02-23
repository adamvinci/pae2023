package be.vinci.pae.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.services.UserDataService;
import be.vinci.pae.utils.ApplicationBinderMock;
import jakarta.ws.rs.WebApplicationException;
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
  private UserDataService userDataService;
  private User userSteven;


  @BeforeEach
  void setUp() {
    userUcc = locator.getService(UserUcc.class);
    userDataService = locator.getService(UserDataService.class);
    userSteven = locator.getService(User.class);
    userSteven = Mockito.mock(UserImpl.class);

    Mockito.when(userSteven.getId()).thenReturn(2);
    Mockito.when(userSteven.getEmail()).thenReturn("steven.agbassah@student.vinci.be");
    Mockito.when(userSteven.getRole()).thenReturn("aidant");
    Mockito.when(userSteven.getPassword()).thenReturn("123*");
    Mockito.when(userSteven.checkPassword("123*")).thenReturn(true);

    Mockito.when(userDataService.getOne(2)).thenReturn(userSteven);
    Mockito.when(userDataService.getOne("steven.agbassah@student.vinci.be")).thenReturn(userSteven);

  }

  @DisplayName("Test login(String email, String password) with good email and good password")
  @Test
  void testLoginWithGoodCredentials() {

    assertEquals(userSteven, userUcc.login("steven.agbassah@student.vinci.be", "123*"));
  }

  @DisplayName("Test login(String email, String password) with good email and bad password")
  @Test
  void testLoginWithGoodEmailAndBadPassword() {
    assertThrows(WebApplicationException.class,
        () -> userUcc.login("steven.agbassah@student.vinci.be", "123"), "Mauvais mot de passe");

  }

  @DisplayName("Test login(String email, String password) with bad email and good password")
  @Test
  void testLoginWithBadEmailAndGoodPassword() {
    Mockito.when(userDataService.getOne("stevenagbassah@student.vinci.be"))
        .thenReturn(new UserImpl());
    assertThrows(WebApplicationException.class,
        () -> userUcc.login("stevenagbassah@student.vinci.be", "123*"), "Cette email n'existe pas");

  }


  @Test
  void getOne() {
  }
}