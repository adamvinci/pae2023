package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.Objet;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.ObjectDAO;
import be.vinci.pae.dal.TypeObjetDAO;
import be.vinci.pae.dal.services.DALTransaction;
import be.vinci.pae.utils.FatalException;
import jakarta.inject.Inject;
import java.util.List;

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
        NotificationDTO notificationCreated = dataServiceNotification.createOne(notification);
        dataServiceNotification.linkNotifToUser(notificationCreated.getId(),
            objetDTO1.getUtilisateur());
      }

      return objetDTO1;
    } catch (FatalException e) {
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
      notification.setMessage(message);
      notification.setType("refus");
      NotificationDTO notificationCreated = dataServiceNotification.createOne(notification);
      if (objetDTO1.getUtilisateur() != null) {
        dataServiceNotification.linkNotifToUser(notificationCreated.getId(),
            objetDTO1.getUtilisateur());
      }

      return objetDTO1;
    } catch (FatalException e) {
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
    } catch (FatalException e) {
      dal.rollBackTransaction();
      throw e;
    } finally {
      dal.commitTransaction();
    }
  }

  @Override
  public ObjetDTO mettreEnVente(ObjetDTO objetDTO) {
    Exception e1 = null;
    try {
      dal.startTransaction();
      Objet objet = (Objet) objetDTO;
      objet.mettreEnVente();
      return dataService.updateObjectState(objetDTO);
    } catch (Exception e) {
      try {
        dal.rollBackTransaction();
      } catch (Exception rollbackException) {
        e.addSuppressed(rollbackException);
      }
      e1 = e;
      throw e;
    } finally {
      try {
        dal.commitTransaction();
      } catch (Exception commitException) {
        if (e1 != null) {
          e1.addSuppressed(commitException);
        } else {
          throw commitException;
        }
      }
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
    } catch (FatalException e) {
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
}
