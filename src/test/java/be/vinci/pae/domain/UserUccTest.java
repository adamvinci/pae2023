package be.vinci.pae.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.UserFactory;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.ApplicationBinderMock;
import be.vinci.pae.utils.exception.ConflictException;
import be.vinci.pae.utils.exception.FatalException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.WebApplicationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
  private DALTransaction dalService;

  private User userLeonHashed;

  private UserFactory userFactory = locator.getService(UserFactory.class);


  @BeforeEach
  void setUp() {
    userUcc = locator.getService(UserUcc.class);
    userDAO = locator.getService(UserDAO.class);
    dalService = locator.getService(DALTransaction.class);

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


  @DisplayName("Test login()  with a FatalException")
  @Test
  void testLoginWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> userUcc.login("stevenagbassah@student.vinci.be",
        "siuuu"));

  }


  @DisplayName("test register with good email")
  @Test
  void testRegisterWhitGoodEmail() {
    Mockito.when(userDAO.getOne("leon.kelmendi@student.vinci.be")).thenReturn(null);
    Mockito.when(userDAO.createOne(userLeon)).thenReturn(userLeon);
    assertNotNull(userUcc.register(userLeon));
  }


  @DisplayName("test register with email already exist")
  @Test
  void testRegisterWhitBadEmail() {
    assertThrows(ConflictException.class, () -> userUcc.register(userMemberSteven),
        "This email already exist");

  }


  @DisplayName("Test register  with a FatalException")
  @Test
  void testRegisterWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> userUcc.register(userLeon));

  }

  @DisplayName("Test getOne(id) with the good id")
  @Test
  void getOneGoodId() {
    assertEquals(userMemberSteven, userUcc.getOne(1));
  }


  @DisplayName("Test getOne with a FatalException")
  @Test
  void testGetOneFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> userUcc.getOne(999));

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
  void makeAdminWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> userUcc.makeAdmin(userMemberSteven));
  }

  @DisplayName("Test makeAdmin with a Conflict")
  @Test
  void makeAdminWithConflictException() {
    doThrow(new NoSuchElementException("exception")).when(userDAO).update(userMemberSteven);
    assertThrows(ConflictException.class, () -> userUcc.makeAdmin(userMemberSteven));
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


  @DisplayName("Test getAll with a FatalException")
  @Test
  void getAllUsersFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> userUcc.getAll());
  }

  @DisplayName("Test getPicture() return null for an non existent id")
  @Test
  void testGetPictureReturnNull() {
    Mockito.when(userDAO.getPicture(1)).thenReturn(null);
    assertNull(userUcc.getPicture(1), "This user  does not exist");
  }

  @DisplayName("Test getPicture() return  a string for an  existent id")
  @Test
  void testGetPictureReturnPathToPicture() {
    Mockito.when(userDAO.getPicture(1)).thenReturn("Path To Picture");
    assertEquals("Path To Picture", userUcc.getPicture(1));

  }

  @DisplayName("Test getPicture()  with a FatalException")
  @Test
  void testGetPictureWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> userUcc.getPicture(1));

  }


  @DisplayName("Test update with a Conflict")
  @Test
  void updateWithAConflict() {
    UserDTO user = userFactory.getUserDTO();
    user.setNom("Lebron");
    user.setPrenom("James");
    user.setEmail("lebron.james@nba.be");
    user.setGsm("123");
    user.setId(1);
    User user1 = (User) user;
    user.setPassword("bm");
    doThrow(new NoSuchElementException("exception")).when(userDAO).update(userMemberSteven);
    assertThrows(ConflictException.class, () -> userUcc.update(user, "123*"));
  }


  @DisplayName("Test update ")
  @Test
  void testUpdate() {
    UserDTO user = userFactory.getUserDTO();
    user.setNom("Lebron");
    user.setPrenom("James");
    user.setEmail("lebron.james@nba.be");
    user.setGsm("123");
    user.setId(1);
    user.setPassword("bm");
    user.setImage("blablabla");

    assertEquals(userMemberSteven.getNom(), "Agbassah");
    assertNotNull(userUcc.update(user, "123*"));
    assertEquals(userMemberSteven.getNom(), "Lebron");
  }

  @DisplayName("Test update with wrong actual password")
  @Test
  void updateWrongPassword() {
    UserDTO user = userFactory.getUserDTO();
    user.setNom("Lebron");
    user.setPrenom("James");
    user.setEmail("lebron.james@nba.be");
    user.setGsm("123");
    user.setId(2);
    user.setPassword("bm");

    UserDTO user2 = userFactory.getUserDTO();
    user2.setNom("Non");
    user2.setPrenom("existing");
    user2.setEmail("non.existing@error.be");
    user2.setGsm("123");
    user2.setId(2);
    user2.setPassword("bm");

    assertThrows(FatalException.class, () -> userUcc.update(user,"124*"));
  }

  @DisplayName("Test update with a FatalException")
  @Test
  void updateWithFatalException() {
    UserDTO user = userFactory.getUserDTO();
    user.setNom("Lebron");
    user.setPrenom("James");
    user.setEmail("lebron.james@nba.be");
    user.setGsm("123");
    user.setId(1);
    user.setPassword("bm");
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> userUcc.update(user, "123*"));
  }

  @DisplayName("Test update with an non existing user (in the DB)")
  @Test
  void updateNonExistingUser() {
    UserDTO user = userFactory.getUserDTO();
    user.setNom("Lebron");
    user.setPrenom("James");
    user.setEmail("lebron.james@nba.be");
    user.setGsm("123");
    user.setId(100);
    user.setPassword("bm");

    Mockito.when(userDAO.getOne(100)).thenReturn(null);
    assertThrows(WebApplicationException.class, () -> userUcc.update(user,"123*"));
  }



}
