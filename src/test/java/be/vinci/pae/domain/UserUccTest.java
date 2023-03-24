package be.vinci.pae.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.UserFactory;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.ApplicationBinderMock;
import be.vinci.pae.utils.FatalException;
import java.time.LocalDate;
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

  private UserUcc userUcc;
  private UserDAO userDAO;
  private User userLeon;
  private User userMemberSteven;

  private User userLeonHashed;

  private UserFactory userFactory = locator.getService(UserFactory.class);

  private DALTransaction dalTransaction;


  @BeforeEach
  void setUp() {
    userUcc = locator.getService(UserUcc.class);
    userDAO = locator.getService(UserDAO.class);
    dalTransaction = locator.getService(DALTransaction.class);

    userMemberSteven = (User) userFactory.getUserDTO();
    userMemberSteven.setEmail("steven.agbassah@student.vinci.be");
    userMemberSteven.setGsm("05678458586");
    userMemberSteven.setId(1);
    userMemberSteven.setNom("Agbassah");
    userMemberSteven.setPrenom("Steven");
    userMemberSteven.setPassword("$2a$10$fYQHAoeC3sQ.AZuBsxJUWuh7miB8QIZ1/gDsdp7zOhg2cmtknqlmy");
    userMemberSteven.setImage("");
    userMemberSteven.setDateInscription(LocalDate.now());
    userMemberSteven.setRole("membre");

    userLeon = (User) userFactory.getUserDTO();
    userLeon.setEmail("leon.kelmendi@student.vinci.be");
    userLeon.setGsm("05678458586");
    userLeon.setId(3);
    userLeon.setNom("kelmendi");
    userLeon.setPrenom("leon");
    userLeon.setPassword("123*");
    userLeon.setImage("");
    userLeon.setDateInscription(LocalDate.now());
    userLeon.setRole("membre");

    userLeonHashed = (User) userFactory.getUserDTO();
    userLeonHashed.setEmail("leon.kelmendi@student.vinci.be");
    userLeonHashed.setGsm("05678458586");
    userLeonHashed.setId(3);
    userLeonHashed.setNom("kelmendi");
    userLeonHashed.setPrenom("leon");
    userLeonHashed.setPassword("$2a$10$fYQHAoeC3sQ.AZuBsxJUWuh7miB8QIZ1/gDsdp7zOhg2cmtknqlmy");
    userLeonHashed.setImage("");
    userLeonHashed.setDateInscription(LocalDate.now());
    userLeonHashed.setRole("membre");

    Mockito.when(userDAO.getOne(1)).thenReturn(userMemberSteven);
    Mockito.when(userDAO.getOne("steven.agbassah@student.vinci.be")).thenReturn(userMemberSteven);
  }

  @DisplayName("Test login(String email, String password) w ith good email and good password")
  @Test
  void testLoginWithGoodCredentials() {

    assertEquals(userMemberSteven,
        userUcc.login("steven.agbassah@student.vinci.be", "123*"));
  }


  @DisplayName("Test login(String email, String password) with good email and bad password")
  @Test
  void testLoginWithGoodEmailAndBadPassword() {
    assertNull(userUcc.login("steven.agbassah@student.vinci.be", "123"), "Bad credentials");

  }


  @DisplayName("Test  login(String email, String password) with bad email ")
  @Test
  void testLoginWithBadEmailAndGoodPassword() {
    Mockito.when(userDAO.getOne("stevenagbassah@student.vinci.be"))
        .thenReturn(null);
    assertNull(userUcc.login("stevenagbassah@student.vinci.be", "123*"),
        "This email does not exists");

  }

  @DisplayName("Test login() with a FatalException")
  @Test
  void testLogintWithFatalException() {
    Mockito.when(userDAO.getOne("de")).thenThrow(FatalException.class);
    Mockito.verify(dalTransaction, Mockito.atMostOnce()).rollBackTransaction();
  }

  @DisplayName("test register with good email")
  @Test
  void testRegisterWhitGoodemail() {
    Mockito.when(userDAO.getOne("leon.kelmendi@student.vinci.be")).thenReturn(null);
    Mockito.when(userDAO.createOne(userLeon)).thenReturn(userLeon);
    assertNotNull(userUcc.register(userLeon));
  }


  @DisplayName("test register with email already exist")
  @Test
  void testRegisterWhitBademail() {
    assertNull(userUcc.register(userMemberSteven));
  }

  @DisplayName("Test register with a FatalException")
  @Test
  void testRegistertWithFatalException() {
    Mockito.when(userDAO.createOne(userLeon)).thenThrow(FatalException.class);
    Mockito.verify(dalTransaction, Mockito.atMostOnce()).rollBackTransaction();
  }

  @DisplayName("Test getOne(id) with the good id")
  @Test
  void getOneGoodId() {
    assertEquals(userMemberSteven, userUcc.getOne(1));
  }

  @DisplayName("Test getOne(id) with a FatalException")
  @Test
  void testGetOneWithFatalException() {
    Mockito.when(userDAO.getOne(1)).thenThrow(FatalException.class);
    Mockito.verify(dalTransaction, Mockito.atMostOnce()).rollBackTransaction();
  }

  @DisplayName("Test make an user which is 'member' admin (= 'aidant')")
  @Test
  void makeAdminWithAGoodUser() {
    Mockito.when(userDAO.update(userMemberSteven)).thenReturn(true);
    assertEquals(userMemberSteven.getRole(), "membre");
    assertTrue(userMemberSteven.checkCanBeAdmin());
    assertEquals(userMemberSteven, userUcc.makeAdmin(userMemberSteven));
    assertEquals(userMemberSteven.getRole(), "aidant");
  }

  @DisplayName("Test make an user which is 'member' admin (= 'aidant')")
  @Test
  void makeAdminWithABadUser() {
    userMemberSteven.setRole("aidant");
    assertEquals(userMemberSteven.getRole(), "aidant");
    assertNull(userUcc.makeAdmin(userMemberSteven));
    assertEquals(userMemberSteven.getRole(), "aidant");
  }

  @DisplayName("Test makeAdmin with a FatalException")
  @Test
  void testMakeAdminWithFatalException() {
    Mockito.when(userDAO.update(userLeon)).thenThrow(FatalException.class);
    Mockito.verify(dalTransaction, Mockito.atMostOnce()).rollBackTransaction();
  }

  @DisplayName("Test getAll() users with a non-empty list(resultset)")
  @Test
  void getAllUsersNonEmpty() {
    List<UserDTO> userDTOList = new ArrayList<>();
    userDTOList.add(userLeon);
    userDTOList.add(userMemberSteven);

    Mockito.when(userDAO.getAll()).thenReturn(userDTOList);
    assertSame(userDTOList, userUcc.getAll());
  }

  @DisplayName("Test getAll() users with a empty list(resultset)")
  @Test
  void getAllUsersEmpty() {
    Mockito.when(userDAO.getAll()).thenReturn(null);
    assertNull(userUcc.getAll(), "Non users in the database");
  }

  @DisplayName("Test getAll() with a FatalException")
  @Test
  void testGetAllUserstWithFatalException() {
    Mockito.when(userDAO.getAll()).thenThrow(FatalException.class);
    Mockito.verify(dalTransaction, Mockito.atMostOnce()).rollBackTransaction();
  }

}