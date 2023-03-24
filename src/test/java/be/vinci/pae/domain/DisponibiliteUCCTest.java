package be.vinci.pae.domain;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import be.vinci.pae.business.dto.DisponibiliteDTO;
import be.vinci.pae.business.factory.DisponibiliteFactory;
import be.vinci.pae.business.ucc.DisponibiliteUCC;
import be.vinci.pae.dal.DisponibiliteDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.ApplicationBinderMock;
import be.vinci.pae.utils.FatalException;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DisponibiliteUCCTest {

  private ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderMock());
  private DisponibiliteDAO disponibiliteDAO;
  private DisponibiliteUCC disponibiliteUCC;
  private DisponibiliteDTO disponibilite;
  private DisponibiliteFactory disponibiliteFactory;

  private DALTransaction dalTransaction;

  @BeforeEach
  void setUp() {
    disponibiliteDAO = locator.getService(DisponibiliteDAO.class);
    disponibiliteUCC = locator.getService(DisponibiliteUCC.class);
    disponibiliteFactory = locator.getService(DisponibiliteFactory.class);
    dalTransaction = locator.getService(DALTransaction.class);
    disponibilite = disponibiliteFactory.getDisponibilite();
    disponibilite.setId(1);

  }

  @DisplayName("Test getDisponibilite() return null when resultset is empty")
  @Test
  void testGetDisponibiliteReturnNull() {
    when(disponibiliteDAO.getAll()).thenReturn(null);
    assertNull(disponibiliteUCC.getDisponibilite(), "No disponiblity saved in the database");
  }

  @DisplayName("Test getDisponibilite() return a list when resultset is not empty")
  @Test
  void testGetDisponibiliteReturnList() {
    List<DisponibiliteDTO> disponibiliteDTOS = new ArrayList<>();
    when(disponibiliteDAO.getAll()).thenReturn(disponibiliteDTOS);
    assertEquals(disponibiliteDTOS, disponibiliteUCC.getDisponibilite());
  }

  @DisplayName("Test getDisponibilite() with a FatalException")
  @Test
  void testGetDisponibiliteWithFatalException() {
    when(disponibiliteDAO.getAll()).thenThrow(FatalException.class);
    Mockito.verify(dalTransaction, Mockito.atMostOnce()).rollBackTransaction();
  }

  @DisplayName("Test getOneDisponibilite( int id ) return null when id does not exist")
  @Test
  void testGetOneDisponibiliteReturnNull() {
    when(disponibiliteDAO.getOne(1)).thenReturn(null);
    assertNull(disponibiliteUCC.getOne(1), "No object corresponding to the id");
  }

  @DisplayName("Test getOneDisponibilite( int id ) return an object when id does  exist")
  @Test
  void testGetOneDisponibiliteReturnDTO() {
    when(disponibiliteDAO.getOne(1)).thenReturn(disponibilite);
    assertEquals(disponibilite, disponibiliteUCC.getOne(1));
  }

  @DisplayName("Test getOneDisponibilite() with a FatalException")
  @Test
  void testGetOneDisponibiliteWithFatalException() {
    doThrow(new FatalException("exception")).when(dalTransaction).startTransaction();
    assertThrows(FatalException.class,()->disponibiliteUCC.getOne(1));

  }
}
