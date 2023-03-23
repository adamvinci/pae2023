package be.vinci.pae.domain;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.business.factory.ObjetFactory;
import be.vinci.pae.business.ucc.ObjetUCC;
import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.ObjectDAO;
import be.vinci.pae.dal.TypeObjetDAO;
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
 * Unit test of {@link be.vinci.pae.business.ucc.ObjetUCCImpl}.
 */
class ObjetUCCTest {

  private ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderMock());

  private ObjetUCC objetUCC;
  private ObjectDAO objectDAO;
  private ObjetDTO objetDTO;
  private ObjetFactory objetFactory;
  private NotificationFactory notificationFactory;
  private NotificationDAO notificationDAO;
  private NotificationDTO notificationDTO;

  private TypeObjetDAO typeObjetDAO;

  @BeforeEach
  void setUp() {
    objetUCC = locator.getService(ObjetUCC.class);
    objectDAO = locator.getService(ObjectDAO.class);
    typeObjetDAO = locator.getService(TypeObjetDAO.class);
    objetFactory = locator.getService(ObjetFactory.class);
    notificationFactory = locator.getService(NotificationFactory.class);
    notificationDAO = locator.getService(NotificationDAO.class);
    objetDTO = objetFactory.getObjet();
    notificationDTO = notificationFactory.getNotification();

  }

  @DisplayName("Test getAllObject() return null when resultset is null")
  @Test
  void testGetAllObjectReturnNull() {
    Mockito.when(objectDAO.getAllObjet()).thenReturn(null);
    assertNull(objetUCC.getAllObject(), "No object in the database");
  }

  // AssertSame to see that it point toward the same object
  // ,if assertEquals we compare the value so we need to take an attribute
  @DisplayName("Test getAllObject() return list when resultset is not empty")
  @Test
  void testGetAllObjectReturnList() {
    List<ObjetDTO> objetDTOList = new ArrayList<>();
    Mockito.when(objectDAO.getAllObjet()).thenReturn(objetDTOList);
    assertEquals(objetDTOList, objetUCC.getAllObject());
  }

  @DisplayName("Test getAllObjectType() return null when resultset is null")
  @Test
  void testGetAllObjectTypeReturnNull() {
    Mockito.when(typeObjetDAO.getAll()).thenReturn(null);
    assertNull(objetUCC.getAllObjectType(), "No type of object in the database");
  }

  @DisplayName("Test getAllObjectType() return list when resultset is not empty")
  @Test
  void testGetAllObjectTypeReturnList() {
    List<TypeObjetDTO> typeObjetDTOList = new ArrayList<>();
    Mockito.when(typeObjetDAO.getAll()).thenReturn(typeObjetDTOList);
    assertEquals(typeObjetDTOList, objetUCC.getAllObjectType());
  }

  @DisplayName("Test getPicture() return null for an non existent id")
  @Test
  void testGetPictureReturnNull() {
    Mockito.when(objectDAO.getPicture(1)).thenReturn(null);
    assertNull(objetUCC.getPicture(1), "This object does not exist");
  }

  @DisplayName("Test getPicture() return  a string for an  existent id")
  @Test
  void testGetPictureReturnPathToPicture() {
    Mockito.when(objectDAO.getPicture(1)).thenReturn("Path To Picture");
    assertEquals("Path To Picture", objetUCC.getPicture(1));

  }

  @DisplayName("Test  getOne(int id) with a non-existent id")
  @Test
  void testGetOneWithBadId() {
    Mockito.when(objectDAO.getOne(1)).thenReturn(null);
    assertNull(objetUCC.getOne(1), "This id doesnt exist");
  }

  @DisplayName("Test  getOne(int id) with a =existent id")
  @Test
  void testGetOneWithGoodId() {
    Mockito.when(objectDAO.getOne(1)).thenReturn(objetDTO);
    assertEquals(objetDTO, objetUCC.getOne(1));
  }

  @DisplayName("Test accepterObjet(ObjetDTO objetDTO) with a bad state")
  @Test
  void testAccepterObjetWithBadState() {
    objetDTO.setEtat("refuser");

    assertNull(objetUCC.accepterObjet(objetDTO, notificationDTO),
        "Return null if accepterObjet() is false");

  }

  @DisplayName("Test accepterObjet(ObjetDTO objetDTO) with a good state and without notification")
  @Test
  void testAccepterObjetWithGoodStateAndWithoutNotification() {
    objetDTO.setEtat("proposer");
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);
    assertAll(
        () -> assertEquals(objetDTO, objetUCC.accepterObjet(objetDTO, notificationDTO)),
        () -> assertEquals("accepte", objetDTO.getEtat())
    );
  }

  @DisplayName("Test accepterObjet(ObjetDTO objetDTO) with a good state and with notification")
  @Test
  void testAccepterObjetWithGoodStateAndWithNotification() {
    objetDTO.setIdObjet(1);
    objetDTO.setEtat("proposer");
    objetDTO.setUtilisateur(1);
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);
    Mockito.when(notificationDAO.createOne(notificationDTO)).thenReturn(notificationDTO);

    assertEquals(objetDTO, objetUCC.accepterObjet(objetDTO, notificationDTO));

  }


  @DisplayName("Test refuserObject(ObjetDTO objetDTO, String message,"
      + " NotificationDTO notification) with a bad state")
  @Test
  void testRefuserObjectWithBadState() {
    objetDTO.setEtat("refuser");
    assertNull(objetUCC.refuserObject(objetDTO, "message", notificationDTO),
        "Return null if refuserObjet() is false");
  }

  @DisplayName("Test refuserObject(ObjetDTO objetDTO, String message,"
      + " NotificationDTO notification) with a good state and the object without registered user")
  @Test
  void testRefuserObjectWithGoodStateAndWithoutUser() {
    objetDTO.setEtat("proposer");
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);
    assertAll(
        () -> assertEquals(objetDTO, objetUCC.refuserObject(objetDTO, "message", notificationDTO)),
        () -> assertEquals("message", notificationDTO.getMessage())
    );
  }

  @DisplayName("Test refuserObject(ObjetDTO objetDTO, String message,"
      + " NotificationDTO notification) with a good state and the object with registered user")
  @Test
  void testRefuserObjectWithGoodStateAndWithUser() {
    objetDTO.setEtat("proposer");
    objetDTO.setUtilisateur(1);
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);

    Mockito.when(notificationDAO.createOne(notificationDTO)).thenReturn(notificationDTO);

    assertAll(
        () -> assertEquals(objetDTO, objetUCC.refuserObject(objetDTO, "message", notificationDTO)),
        () -> assertEquals("message", notificationDTO.getMessage())
    );
  }

  @DisplayName("Test vendreObject(ObjetDTO objetDTO) with a bad state")
  @Test
  void testVendreObjectWithBadState() {
    objetDTO.setEtat("accepte");
    assertNull(objetUCC.vendreObject(objetDTO), "The state need to be 'en vente' to be accepted");
  }

  @DisplayName("Test vendreObject(ObjetDTO objetDTO) with a good state")
  @Test
  void testVendreObjectWithGoodState() {
    objetDTO.setEtat("en vente");
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);
    assertAll(
        () -> assertEquals(objetDTO, objetUCC.vendreObject(objetDTO)),
        () -> assertEquals("vendu", objetDTO.getEtat())
    );
  }
}