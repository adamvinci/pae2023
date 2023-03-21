package be.vinci.pae.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.domaine.UserImpl;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.utils.ApplicationBinderMock;
import java.util.ArrayList;
import java.util.List;
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
  List<UserDTO> users = new ArrayList<>();
  private UserUcc userUcc;
  private UserDAO userDAO;
  private User userSteven;
  private User userLeon;
  private User userLeonHash;

  @BeforeEach
  void setUp() {
    userUcc = locator.getService(UserUcc.class);
    userDAO = locator.getService(UserDAO.class);
    userSteven = Mockito.mock(UserImpl.class);
    userLeonHash = Mockito.mock(UserImpl.class);
    userLeon = Mockito.mock(UserImpl.class);

    Mockito.when(userSteven.getId()).thenReturn(2);
    Mockito.when(userSteven.getEmail()).thenReturn("steven.agbassah@student.vinci.be");
    Mockito.when(userSteven.getRole()).thenReturn("aidant");
    Mockito.when(userSteven.getPassword()).thenReturn("123*");
    Mockito.when(userSteven.checkPassword("123*")).thenReturn(true);

    Mockito.when(userLeon.getId()).thenReturn(3);
    Mockito.when(userLeon.getEmail()).thenReturn("leon.kelmendi@student.vinci.be");
    Mockito.when(userLeon.getRole()).thenReturn("membre");
    Mockito.when(userLeon.getPassword()).thenReturn("123*");
    Mockito.when(userLeon.getNom()).thenReturn("leon");
    Mockito.when(userLeon.getPrenom()).thenReturn("kelmendi");
    Mockito.when(userLeon.getGsm()).thenReturn("123");

    Mockito.when(userLeon.checkCanBeAdmin()).thenReturn(true);
    Mockito.when(userLeon.changeToAdmin()).thenReturn(true);

    Mockito.when(userLeonHash.getId()).thenReturn(3);
    Mockito.when(userLeonHash.getEmail()).thenReturn("leon.kelmendi@student.vinci.be");
    Mockito.when(userLeonHash.getRole()).thenReturn("membre").thenReturn("aidant");
    Mockito.when(userLeonHash.getPassword()).thenReturn("123*");
    Mockito.when(userLeonHash.getNom()).thenReturn("leon");
    Mockito.when(userLeonHash.getPrenom())
        .thenReturn("$2a$10$fYQHAoeC3sQ.AZuBsxJUWuh7miB8QIZ1/gDsdp7zOhg2cmtknqlmy");
    Mockito.when(userLeonHash.getGsm()).thenReturn("123");

    Mockito.when(userDAO.getOne(2)).thenReturn(userSteven);
    Mockito.when(userDAO.getOne("steven.agbassah@student.vinci.be")).thenReturn(userSteven);
    Mockito.when(userDAO.createOne(userLeon)).thenReturn(userLeonHash);

    users.add(userSteven);
    users.add(userLeon);
    Mockito.when(userDAO.getAll()).thenReturn(users);
    Mockito.when(userDAO.update(userLeon)).thenReturn(true);

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

  @DisplayName("test register with good email")
  @Test
  void testRegisterWhitGoodemail() {
    assertNotNull(userUcc.register(userLeon));
  }

  @DisplayName("test register with email already exist")
  @Test
  void testRegisterWhitBademail() {

    assertNull(userUcc.register(userSteven));
  }

  @DisplayName("Test getOne(id) with the good id")
  @Test
  void getOneGoodId() {
    assertEquals(userSteven.getEmail(), userUcc.getOne(2).getEmail());
  }


  @DisplayName("Test getAll()")
  @Test
  void getAllTest() {
    assertEquals(users, userUcc.getAll());
  }


  @DisplayName("Test make an user which is 'member' admin (= 'aidant')")
  @Test
  void makeAdminWithAGoodUser() {
    assertEquals(userLeon.getRole(), userUcc.makeAdmin(userLeon).getRole());
//    assertEquals(userLeon.getRole(), "aidant");
  }


}