package be.vinci.pae.domain;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.business.factory.ObjetFactory;
import be.vinci.pae.business.factory.TypeObjetFactory;
import be.vinci.pae.business.ucc.ObjetUCC;
import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.ObjectDAO;
import be.vinci.pae.dal.TypeObjetDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.ApplicationBinderMock;
import be.vinci.pae.utils.exception.BusinessException;
import be.vinci.pae.utils.exception.ConflictException;
import be.vinci.pae.utils.exception.FatalException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
  private TypeObjetDTO typeObjetDTO;
  private TypeObjetFactory typeFactory;
  private ObjetDTO objetDTO;
  private ObjetFactory objetFactory;
  private NotificationFactory notificationFactory;
  private NotificationDAO notificationDAO;
  private NotificationDTO notificationDTO;
  private TypeObjetDAO typeObjetDAO;

  private DALTransaction dalService;

  @BeforeEach
  void setUp() {
    objetUCC = locator.getService(ObjetUCC.class);
    objectDAO = locator.getService(ObjectDAO.class);
    typeObjetDAO = locator.getService(TypeObjetDAO.class);
    objetFactory = locator.getService(ObjetFactory.class);
    typeFactory = locator.getService(TypeObjetFactory.class);
    notificationFactory = locator.getService(NotificationFactory.class);
    notificationDAO = locator.getService(NotificationDAO.class);
    objetDTO = objetFactory.getObjet();
    notificationDTO = notificationFactory.getNotification();
    dalService = locator.getService(DALTransaction.class);
    typeObjetDTO = typeFactory.getTypeObjet();


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

  @DisplayName("Test getAllObject()  with a FatalException")
  @Test
  void testGetAllObjectWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.getAllObject());

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

  @DisplayName("Test getAllObjectType()  with a FatalException")
  @Test
  void testGetAllObjectTypeWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.getAllObjectType());

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

  @DisplayName("Test getPicture()  with a FatalException")
  @Test
  void testGetPictureWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.getPicture(1));

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

  @DisplayName("Test getOne(int id)  with a FatalException")
  @Test
  void testGetOneObjectWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.getOne(1));

  }

  @DisplayName("Test  getOneType(int id) with a non-existent id")
  @Test
  void testGetOneTypeWithBadId() {
    Mockito.when(typeObjetDAO.getOne(1)).thenReturn(null);
    assertNull(objetUCC.getOneType(1), "This id doesnt exist");
  }

  @DisplayName("Test  getOneType(int id) with a =existent id")
  @Test
  void testGetOneTypeWithGoodId() {
    Mockito.when(typeObjetDAO.getOne(1)).thenReturn(typeObjetDTO);
    assertEquals(typeObjetDTO, objetUCC.getOneType(1));
  }

  @DisplayName("Test getOneType(int id)  with a FatalException")
  @Test
  void testGetOneTypeObjectWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.getOneType(1));

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

  @DisplayName("Test accepterObjet(ObjetDTO objetDTO) with a FatalException")
  @Test
  void testAccepterObjetWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.accepterObjet(objetDTO, notificationDTO));

  }

  @DisplayName("Test accepterObject with a ConflictException")
  @Test
  void testAccepterPropositionWithConflictException() {
    objetDTO.setEtat("proposer");
    doThrow(new NoSuchElementException("exception")).when(objectDAO).updateObjectState(objetDTO);
    assertThrows(ConflictException.class, () -> objetUCC.accepterObjet(objetDTO, notificationDTO));
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
        () -> assertEquals(
            "Votre Objet (" + objetDTO.getDescription() + ") a été refusé car: " + "message",
            notificationDTO.getMessage())
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
        () -> assertEquals(
            "Votre Objet (" + objetDTO.getDescription() + ") a été refusé car: " + "message",
            notificationDTO.getMessage())
    );
  }

  @DisplayName("Test  refuserObject(ObjetDTO objetDTO, "
      + "String message,NotificationDTO notification) with a fatalException")
  @Test
  void testRefuserObjetWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class,
        () -> objetUCC.refuserObject(objetDTO, "refus", notificationDTO));
  }

  @DisplayName("Test refuserObject with a ConflictException")
  @Test
  void testRefuserPropositionWithConflictException() {
    objetDTO.setEtat("proposer");
    doThrow(new NoSuchElementException("exception")).when(objectDAO).updateObjectState(objetDTO);
    assertThrows(ConflictException.class, () -> objetUCC.refuserObject(objetDTO,
        "message", notificationDTO));
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

  @DisplayName("Test vendreObject(ObjetDTO objetDTO) with a FatalException")
  @Test
  void testVendreObjectWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.vendreObject(objetDTO));
  }

  @DisplayName("Test vendreObject with a ConflictException")
  @Test
  void testVendreObjectWithConflictException() {
    objetDTO.setEtat("en vente");
    doThrow(new NoSuchElementException("exception")).when(objectDAO).updateObjectState(objetDTO);
    assertThrows(ConflictException.class, () -> objetUCC.vendreObject(objetDTO));
  }

  @DisplayName("Test depotObject(ObjetDTO objetDTO) with bad state")
  @Test
  void testDepotObjectwithBadStateAndLocalisationMagasin() {
    objetDTO.setEtat("proposer");
    objetDTO.setLocalisation("Atelier");
    assertNull(objetUCC.depotObject(objetDTO), "The state must be 'accepted'");
  }

  @DisplayName("Test depotObject(ObjetDTO objetDTO) with bad state")
  @Test
  void testDepotObjectwithBadStateAndLocalisationAtelier() {
    objetDTO.setEtat("proposer");
    objetDTO.setLocalisation("Magasin");
    assertNull(objetUCC.depotObject(objetDTO), "The state must be 'accepted'");
  }

  @DisplayName("Test depotObject(ObjetDTO objetDTO) with good "
      + "state and localisation = magasin")
  @Test
  void testDepotObjectwithGoodStateAndLocalisationMagasin() {
    objetDTO.setEtat("accepte");
    objetDTO.setLocalisation("Magasin");
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);
    assertEquals(objetDTO, objetUCC.depotObject(objetDTO));
  }

  @DisplayName("Test depotObject(ObjetDTO objetDTO) with good "
      + "state and localisation = atelier")
  @Test
  void testDepotObjectwithGoodStateAndLocalisationAtelier() {
    objetDTO.setEtat("accepte");
    objetDTO.setLocalisation("Atelier");
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);
    assertEquals(objetDTO, objetUCC.depotObject(objetDTO));
  }


  @DisplayName("Test depotObject(ObjetDTO objetDTO) with a FatalException")
  @Test
  void testDepotObjectWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.depotObject(objetDTO));
  }

  @DisplayName("Test depotObject with a ConflictException")
  @Test
  void testDepotObjectWithConflictException() {
    objetDTO.setEtat("accepte");
    objetDTO.setLocalisation("Atelier");
    doThrow(new NoSuchElementException("exception")).when(objectDAO).updateObjectState(objetDTO);
    assertThrows(ConflictException.class, () -> objetUCC.depotObject(objetDTO));
  }

  @DisplayName("Test mettreEnVente(ObjetDTO objetDTO) with a bad state")
  @Test
  void testMettreEnVenteWithABadState() {
    objetDTO.setEtat("proposer");
    assertThrows(BusinessException.class, () -> objetUCC.mettreEnVente(objetDTO),
        "to put an object at sell its statut must be 'accepte");
  }

  @DisplayName("Test mettreEnVente(ObjetDTO objetDTO) with a good state but bad localisation")
  @Test
  void testMettreEnVenteWithAGoodStateBadLocalisation() {
    objetDTO.setEtat("accepte");
    objetDTO.setLocalisation("Atelier");
    assertThrows(BusinessException.class, () -> objetUCC.mettreEnVente(objetDTO),
        "to put an object at sell its need to be deposited in the sto");
  }

  @DisplayName("Test mettreEnVente(ObjetDTO objetDTO) with good params")
  @Test
  void testMettreEnVenteWithGoodParams() {
    objetDTO.setEtat("accepte");
    objetDTO.setLocalisation("Magasin");
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);
    assertEquals(objetDTO, objetUCC.mettreEnVente(objetDTO));
  }

  @DisplayName("Test mettreEnVente(ObjetDTO objetDTO)with a FatalException")
  @Test
  void testMettreEnVenteWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.mettreEnVente(objetDTO));
  }

  @DisplayName("Test mettreEnVente with a ConflictException")
  @Test
  void testMettreEnVenteObjectWithConflictException() {
    objetDTO.setEtat("accepte");
    objetDTO.setLocalisation("Magasin");
    doThrow(new NoSuchElementException("exception")).when(objectDAO).updateObjectState(objetDTO);
    assertThrows(ConflictException.class, () -> objetUCC.mettreEnVente(objetDTO));
  }

  @DisplayName("Test retirerVente() with a FatalException")
  @Test
  void testRetirerVenteWithAFatalException() {
    List<ObjetDTO> objetDTOS = new ArrayList<>();
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.retirerObjetVente(objetDTOS));
  }

  @DisplayName("Test retirerVente() with a badState")
  @Test
  void testRetirerVenteWithABadState() {
    List<ObjetDTO> objetDTOList = new ArrayList<>();
    objetDTO.setEtat("accepte");
    objetDTOList.add(objetDTO);
    assertThrows(BusinessException.class, () -> objetUCC.retirerObjetVente(objetDTOList),
        "Impossible changement, to remove an object from sell "
            + "it state must be 'en vente'Impossible changement, "
            + "to remove an object from sell it state must be 'en vente'");
  }

  @DisplayName("Test retirerVente() with a good state but bad date")
  @Test
  void testRetirerVenteWithAGoodStateAndBadDate() {
    List<ObjetDTO> objetDTOList = new ArrayList<>();
    objetDTO.setEtat("en vente");
    objetDTO.setDate_depot(LocalDate.now());
    objetDTOList.add(objetDTO);
    assertThrows(BusinessException.class, () -> objetUCC.retirerObjetVente(objetDTOList),
        "Impossible changement, to remove an object "
            + "from sell it must be deposited for more than 30days");
  }

  @DisplayName("Test retirerVente() with a good state and good date")
  @Test
  void testRetirerVenteWithAGoodStateAndGoodDate() {
    List<ObjetDTO> objetDTOList = new ArrayList<>();
    objetDTO.setEtat("en vente");
    objetDTO.setDate_depot(LocalDate.of(2022, Month.APRIL, 1));
    objetDTOList.add(objetDTO);
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);
    objetUCC.retirerObjetVente(objetDTOList);
    assertEquals("retirer", objetDTO.getEtat());
  }

  @DisplayName("Test AjouterObjet() with a good object")
  @Test
  void testAjouterObjet() {
    Mockito.when(objectDAO.createObjet(objetDTO)).thenReturn(objetDTO);
    Mockito.when(notificationDAO.createOne(notificationDTO)).thenReturn(notificationDTO);
    //  notificationDTO.setId(1);
    objetUCC.ajouterObjet(objetDTO, notificationDTO);
  }

  @DisplayName("Test ajouterObjet() with a FatalException")
  @Test
  void testAjouterObjetWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.ajouterObjet(objetDTO, notificationDTO));

  }

  @DisplayName("Test updateObject(ObjetDTO objetDTO) with a FatalException")
  @Test
  void testUpdateObjetWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.updateObject(objetDTO));

  }

  @DisplayName("Test updateObject(ObjetDTO objetDTO) ")
  @Test
  void testUpdateObjet() {
    Mockito.when(objectDAO.updateObject(objetDTO)).thenReturn(objetDTO);
    assertEquals(objetDTO, objetUCC.updateObject(objetDTO));

  }

  @DisplayName("Test updateObject(ObjetDTO objetDTO) with a ConflictException")
  @Test
  void testUpdateObjetWithConflictException() {
    doThrow(new NoSuchElementException("exception")).when(objectDAO).updateObject(objetDTO);
    assertThrows(ConflictException.class, () -> objetUCC.updateObject(objetDTO));
  }

  @DisplayName("Test vendreObjectAdmin(ObjetDTO objetDTO) with a bad state")
  @Test
  void testVendreObjectAdminWithABadState() {
    objetDTO.setEtat("proposer");
    assertThrows(BusinessException.class, () -> objetUCC.vendreObjectAdmin(objetDTO),
        "to put an object at sell its statut must be 'accepte");
  }

  @DisplayName("Test vendreObjectAdmin(ObjetDTO objetDTO) with a good state but bad localisation")
  @Test
  void testVendreObjectAdminWithAGoodStateBadLocalisation() {
    objetDTO.setEtat("accepte");
    objetDTO.setLocalisation("Atelier");
    assertThrows(BusinessException.class, () -> objetUCC.vendreObjectAdmin(objetDTO),
        "to put an object at sell its need to be deposited in the sto");
  }

  @DisplayName("Test vendreObjectAdmin(ObjetDTO objetDTO) with good params")
  @Test
  void testVendreObjectAdminWithGoodParams() {
    objetDTO.setEtat("accepte");
    objetDTO.setLocalisation("Magasin");
    Mockito.when(objectDAO.updateObjectState(objetDTO)).thenReturn(objetDTO);
    assertEquals("vendu", objetUCC.vendreObjectAdmin(objetDTO).getEtat());
  }

  @DisplayName("Test vendreObjectAdmin(ObjetDTO objetDTO)with a FatalException")
  @Test
  void testVendreObjectAdminWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> objetUCC.vendreObjectAdmin(objetDTO));
  }

  @DisplayName("Test vendreObjectAdmin with a ConflictException")
  @Test
  void testVendreObjectAdminWithConflictException() {
    objetDTO.setEtat("accepte");
    objetDTO.setLocalisation("Magasin");
    doThrow(new NoSuchElementException("exception")).when(objectDAO).updateObjectState(objetDTO);
    assertThrows(ConflictException.class, () -> objetUCC.vendreObjectAdmin(objetDTO));
  }

}
