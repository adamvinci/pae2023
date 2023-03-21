package be.vinci.pae.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.business.domaine.User;
import be.vinci.pae.business.dto.UserDTO;
import be.vinci.pae.business.factory.UserFactory;
import be.vinci.pae.business.ucc.UserUcc;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.utils.ApplicationBinderMock;
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


  private UserFactory userFactory = locator.getService(UserFactory.class);

  private User userMemberSteven;

  @BeforeEach
  void setUp() {
    userUcc = locator.getService(UserUcc.class);
    userDAO = locator.getService(UserDAO.class);

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
    userLeon.setPassword("$2a$10$fYQHAoeC3sQ.AZuBsxJUWuh7miB8QIZ1/gDsdp7zOhg2cmtknqlmy");
    userLeon.setImage("");
    userLeon.setDateInscription(LocalDate.now());
    userLeon.setRole("membre");

    Mockito.when(userDAO.getOne(1)).thenReturn(userMemberSteven);
    Mockito.when(userDAO.getOne("steven.agbassah@student.vinci.be")).thenReturn(userMemberSteven);
  }

  @DisplayName("Test login(String email, String password) with good email and good password")
  @Test
  void testLoginWithGoodCredentials() {
    assertEquals(userMemberSteven,
        userUcc.login("steven.agbassah@student.vinci.be", "123*"));
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

//  @DisplayName("test register with good email")
//  @Test
//  void testRegisterWhitGoodemail() {
//    assertNotNull(userUcc.register(userLeon));
//  }

  @DisplayName("test register with email already exist")
  @Test
  void testRegisterWhitBademail() {
    assertNull(userUcc.register(userMemberSteven));
  }

  @DisplayName("Test getOne(id) with the good id")
  @Test
  void getOneGoodId() {
    assertEquals(userMemberSteven, userUcc.getOne(1));
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


}