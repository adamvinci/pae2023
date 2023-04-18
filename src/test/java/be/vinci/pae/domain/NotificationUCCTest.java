package be.vinci.pae.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.ApplicationBinderMock;
import be.vinci.pae.utils.exception.FatalException;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NotificationUCCTest {
  private ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderMock());
  private NotificationUCC notificationUCC;
  private NotificationDAO notificationDAO;
  private NotificationDTO notificationDTO;
  private NotificationFactory notificationFactory;
  private DALTransaction dalService;

  @BeforeEach
  void setUp() {
    notificationUCC=locator.getService(NotificationUCC.class);
    notificationDAO=locator.getService(NotificationDAO.class);
    notificationFactory=locator.getService(NotificationFactory.class);
    notificationDTO=notificationFactory.getNotification();
    dalService=locator.getService(DALTransaction.class);
  }

  @DisplayName("Test getAllNotificationsByUser(int) return null when resultset is null")
  @Test
  public void testGetAllNotificationsByUserReturnNull(){
    Mockito.when(notificationDAO.findNotificationsByUser(1)).thenReturn(null);
    assertNull(notificationUCC.getAllNotificationByUser(1),"No notifications in the database");
  }
  @DisplayName("Test getAllNotificationsByUser(int) return list when resultset is not null")
  @Test
  public void testGetAllNotificationsByUserReturnList(){
    List<NotificationDTO> notificationDTOList = new ArrayList<>();
    Mockito.when(notificationDAO.findNotificationsByUser(1)).thenReturn(notificationDTOList);
    assertEquals(notificationDTOList, notificationUCC.getAllNotificationByUser(1));
  }
  @DisplayName("Test getAllNotificationsByUser(int)  with a FatalException")
  @Test
  void testGetAllNotificationsByUserWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> notificationUCC.getAllNotificationByUser(1));
  }
  @DisplayName("Test SetLueNotification(NotificationDTO,int)  with good params")
  @Test
  void testSetLueNotificationWithGoodParams(){
    int utilisateur = 1;
    notificationDTO.setLue(true);
    Mockito.when(notificationDAO.setLueNotification(notificationDTO,utilisateur)).thenReturn(notificationDTO);
    assertEquals(notificationDTO, notificationUCC.setLueNotification(notificationDTO,utilisateur));
  }
  @DisplayName("Test setLueNotification(NotificationDTO,int) with a FatalException")
  @Test
  void testSetLueNotificationWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> notificationUCC.setLueNotification(notificationDTO,1));
  }

}