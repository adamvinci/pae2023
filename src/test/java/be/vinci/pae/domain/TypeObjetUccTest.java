package be.vinci.pae.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.factory.TypeObjetFactory;
import be.vinci.pae.business.ucc.TypeObjetUcc;
import be.vinci.pae.dal.TypeObjetDAO;
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

/**
 * Unit test of {@link be.vinci.pae.business.ucc.TypeObjetUccImpl}.
 */
class TypeObjetUccTest {

  private ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinderMock());
  private TypeObjetDAO typeObjetDAO;
  private TypeObjetDTO typeObjetDTO;
  private TypeObjetUcc typeObjetUcc;
  private TypeObjetFactory typeFactory;

  private DALTransaction dalService;

  @BeforeEach
  void setUp() {
    dalService = locator.getService(DALTransaction.class);
    typeObjetDAO = locator.getService(TypeObjetDAO.class);
    typeObjetUcc = locator.getService(TypeObjetUcc.class);
    typeFactory = locator.getService(TypeObjetFactory.class);
    typeObjetDTO = typeFactory.getTypeObjet();

  }

  @DisplayName("Test getAllObjectType() return null when resultset is null")
  @Test
  void testGetAllObjectTypeReturnNull() {
    Mockito.when(typeObjetDAO.getAll()).thenReturn(null);
    assertNull(typeObjetUcc.getAllObjectType(), "No type of object in the database");
  }

  @DisplayName("Test getAllObjectType() return list when resultset is not empty")
  @Test
  void testGetAllObjectTypeReturnList() {
    List<TypeObjetDTO> typeObjetDTOList = new ArrayList<>();
    Mockito.when(typeObjetDAO.getAll()).thenReturn(typeObjetDTOList);
    assertEquals(typeObjetDTOList, typeObjetUcc.getAllObjectType());
  }

  @DisplayName("Test getAllObjectType()  with a FatalException")
  @Test
  void testGetAllObjectTypeWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> typeObjetUcc.getAllObjectType());

  }

  @DisplayName("Test  getOneType(int id) with a non-existent id")
  @Test
  void testGetOneTypeWithBadId() {
    Mockito.when(typeObjetDAO.getOne(1)).thenReturn(null);
    assertNull(typeObjetUcc.getOneType(1), "This id doesnt exist");
  }

  @DisplayName("Test  getOneType(int id) with a =existent id")
  @Test
  void testGetOneTypeWithGoodId() {
    Mockito.when(typeObjetDAO.getOne(1)).thenReturn(typeObjetDTO);
    assertEquals(typeObjetDTO, typeObjetUcc.getOneType(1));
  }

  @DisplayName("Test getOneType(int id)  with a FatalException")
  @Test
  void testGetOneTypeObjectWithFatalException() {
    doThrow(new FatalException("exception")).doNothing().when(dalService).startTransaction();
    assertThrows(FatalException.class, () -> typeObjetUcc.getOneType(1));

  }
}