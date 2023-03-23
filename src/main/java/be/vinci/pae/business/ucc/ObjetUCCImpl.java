package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.Objet;
import be.vinci.pae.business.dto.NotificationDTO;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.ObjectDAO;
import be.vinci.pae.dal.TypeObjetDAO;
import jakarta.inject.Inject;
import java.time.LocalDate;
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
  private NotificationFactory notifFactory;
  @Inject
  private TypeObjetDAO typeObjetDAO;

  @Override
  public List<ObjetDTO> getAllObject() {

    return dataService.getAllObjet();
  }

  @Override
  public List<TypeObjetDTO> getAllObjectType() {
    return typeObjetDAO.getAll();
  }

  @Override
  public String getPicture(int id) {
    return dataService.getPicture(id);
  }


  @Override
  public ObjetDTO accepterObjet(ObjetDTO objetDTO, NotificationDTO notification) {
    Objet objet = (Objet) objetDTO;
    if (!objet.accepterObjet()) {
      return null;
    }
    ObjetDTO objetDTO1 = dataService.updateObjectState(objetDTO);

    if (objetDTO1.getUtilisateur() != null) {
      notification.setObject(objetDTO1.getIdObjet());
      notification.setMessage("l'objet n° : " + objetDTO1.getIdObjet() + " a été ajouté");
      notification.setType("acceptation");
      NotificationDTO notificationCreated = dataServiceNotification.createOne(notification);
      dataServiceNotification.linkNotifToUser(notificationCreated.getId(),
          objetDTO1.getUtilisateur());
    }

    return objetDTO1;
  }

  @Override
  public ObjetDTO refuserObject(ObjetDTO objetDTO, String message, NotificationDTO notification) {
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
  }

  @Override
  public ObjetDTO depotObject(ObjetDTO objetDTO, String localisation) {
    Objet objet = (Objet) objetDTO;
    if (localisation.equals("Magasin")) {
      if (!objet.deposerEnMagasin()) {
        return null;
      }
    } else {
      if (!objet.deposerEnAtelier()) {
        return null;
      }
    }
    return dataService.updateObjectState(objetDTO);
  }

  @Override
  public ObjetDTO mettreEnVente(ObjetDTO objetDTO) {
    Objet objet = (Objet) objetDTO;

    if (!objet.mettreEnVente()) {
      return null;
    }
    LocalDate today = LocalDate.now();

    objet.setDate_vente(today);

    NotificationDTO notification = notifFactory.getNotification();
    notification.setObject(objetDTO.getIdObjet());
    notification.setMessage("l'objet n° : " + objetDTO.getIdObjet() + " a été mis en vente");
    notification.setType("information");
    ObjetDTO objetDTO1 = dataService.updateObjectState(objetDTO);
    dataServiceNotification.createOne(notification);
    return objetDTO1;
  }

  @Override
  public ObjetDTO vendreObject(ObjetDTO objetDTO) {

    Objet objet = (Objet) objetDTO;
    if (!objet.vendreObjet()) {
      return null;
    }

    return dataService.updateObjectState(objetDTO);
  }

  @Override
  public ObjetDTO getOne(int id) {
    return dataService.getOne(id);
  }
}
