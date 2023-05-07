package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.Objet;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.ObjectDAO;
import be.vinci.pae.dal.TypeObjetDAO;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.MyLogger;
import be.vinci.pae.utils.exception.ConflictException;
import be.vinci.pae.utils.exception.FatalException;
import jakarta.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of {@link ObjetUCC}.
 */
public class ObjetUCCImpl implements ObjetUCC {

  @Inject
  private ObjectDAO dataService;
  @Inject
  private NotificationDAO dataServiceNotification;
  @Inject
  private TypeObjetDAO typeObjetDAO;
  @Inject
  private UserDAO userDAO;

  @Inject
  private DALTransaction dal;

  @Override
  public List<ObjetDTO> getAllObject() {
    try {
      dal.startTransaction();
      return dataService.getAllObjet();
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public List<TypeObjetDTO> getAllObjectType() {
    try {
      dal.startTransaction();
      return typeObjetDAO.getAll();
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public String getPicture(int id) {
    try {
      dal.startTransaction();
      return dataService.getPicture(id);
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }


  @Override
  public ObjetDTO accepterObjet(ObjetDTO objetDTO, NotificationDTO notification) {
    try {
      dal.startTransaction();
      Objet objet = (Objet) objetDTO;
      if (!objet.accepterObjet()) {
        return null;
      }
      ObjetDTO objetDTO1 = dataService.updateObjectState(objetDTO);

      if (objetDTO1.getUtilisateur() != null) {
        notification.setObject(objetDTO1.getIdObjet());
        notification.setType("acceptation");
        notification.setMessage("Votre Objet (" + objetDTO.getDescription() + ") a été accepté");
        NotificationDTO notificationCreated = dataServiceNotification.createOne(notification);
        dataServiceNotification.linkNotifToUser(notificationCreated.getId(),
            objetDTO1.getUtilisateur());
      }
      return objetDTO1;
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }

  }

  @Override
  public ObjetDTO refuserObject(ObjetDTO objetDTO, String message, NotificationDTO notification) {
    try {
      dal.startTransaction();
      Objet objet = (Objet) objetDTO;
      if (!objet.refuserObjet()) {
        return null;
      }
      ObjetDTO objetDTO1 = dataService.updateObjectState(objetDTO);

      notification.setObject(objetDTO1.getIdObjet());
      notification.setMessage(
          "Votre Objet (" + objetDTO.getDescription() + ") a été refusé car: " + message);
      notification.setType("refus");
      NotificationDTO notificationCreated = dataServiceNotification.createOne(notification);
      if (objetDTO1.getUtilisateur() != null) {
        dataServiceNotification.linkNotifToUser(notificationCreated.getId(),
            objetDTO1.getUtilisateur());
      }
      return objetDTO1;
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }

  }

  @Override
  public ObjetDTO depotObject(ObjetDTO objetDTO) {
    try {
      dal.startTransaction();
      Objet objet = (Objet) objetDTO;

      if (!objet.deposer()) {
        return null;
      }
      return dataService.updateObjectState(objetDTO);
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public ObjetDTO mettreEnVente(ObjetDTO objetDTO) {
    try {
      dal.startTransaction();
      Objet objet = (Objet) objetDTO;
      objet.mettreEnVente();

      return dataService.updateObjectState(objetDTO);
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }


  }

  @Override
  public ObjetDTO vendreObject(ObjetDTO objetDTO) {
    try {
      dal.startTransaction();
      Objet objet = (Objet) objetDTO;
      if (!objet.vendreObjet()) {
        return null;
      }
      return dataService.updateObjectState(objetDTO);
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }


  }

  @Override
  public ObjetDTO vendreObjectAdmin(ObjetDTO objetDTO) {
    try {
      dal.startTransaction();
      Objet objet = (Objet) objetDTO;
      objet.vendreObjetAdmin();
      return dataService.updateObjectState(objetDTO);
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public ObjetDTO getOne(int id) {
    try {
      dal.startTransaction();
      return dataService.getOne(id);
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }

  }

  @Override
  public void retirerObjetVente(List<ObjetDTO> listObjectToDelete) {

    try {
      dal.startTransaction();
      for (ObjetDTO objetDTO : listObjectToDelete) {
        Objet objet = (Objet) objetDTO;
        objet.retirerVente();
        dataService.updateObjectState(objetDTO);
        Logger.getLogger(MyLogger.class.getName())
            .log(Level.INFO,
                "Object " + objetDTO.getIdObjet() + " removed from sell");
      }

    } catch (Exception e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public ObjetDTO ajouterObjet(ObjetDTO objetDTO, NotificationDTO notification) {
    try {
      dal.startTransaction();
      Objet objet = (Objet) objetDTO;
      objet.initierEtat();
      ObjetDTO objetDATA = dataService.createObjet(objetDTO);
      notification.setObject(objetDATA.getIdObjet());
      notification.setType("alerteProposition");
      notification.setMessage("l'Objet ( " + objetDATA.getDescription() + ") a été proposé");
      NotificationDTO notificationCreated = dataServiceNotification.createOne(notification);
      dataServiceNotification.linkNotifToAidantAndResponsable(notificationCreated.getId());
      return objetDATA;
    } catch (FatalException e) {
      dal.rollBackTransaction();

      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public TypeObjetDTO getOneType(int id) {
    try {
      dal.startTransaction();
      return typeObjetDAO.getOne(id);
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public ObjetDTO updateObject(ObjetDTO objetDTO) {
    try {
      dal.startTransaction();
      ObjetDTO objetDTO1 = dataService.updateObject(objetDTO);
      dal.commitTransaction();

      return objetDTO1;

    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        throw new ConflictException("Bad version number, retry");
      }
      dal.rollBackTransaction();
      throw e;
    }
  }
}
