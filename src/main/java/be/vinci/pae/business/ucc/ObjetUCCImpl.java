package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.NotificationImpl;
import be.vinci.pae.business.domaine.Objet;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
import be.vinci.pae.business.factory.NotificationFactory;
import be.vinci.pae.business.factory.NotificationFactoryImpl;
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
  public ObjetDTO accepterObjet(ObjetDTO objetDTO){


    LocalDate today = LocalDate.now();

    Objet objet=(Objet) objetDTO;
    objet.accepterObjet();
    NotificationImpl notification= (NotificationImpl) notifFactory.getNotification();
    notification.setObject(objetDTO.getIdObjet());
    notification.setMessage("l'objet n° : "+objetDTO.getIdObjet()+" a été ajouté");
    notification.setType("information");
    if(!dataService.updateObjectState(objet) && !dataServiceNotification.createOne(notification)) return null;

    return objet;
  }
  @Override
  public ObjetDTO refuserObject(ObjetDTO objetDTO){

    return null;
  }
  @Override
  public ObjetDTO depotObject(ObjetDTO objetDTO){


    LocalDate today = LocalDate.now();
    Objet objet=(Objet) objetDTO;

    objet.setDate_depot(today);

    NotificationImpl notification= (NotificationImpl) notifFactory.getNotification();
    notification.setObject(objetDTO.getIdObjet());
    notification.setMessage("l'objet n° : "+objetDTO.getIdObjet()+" a été deposé en magasin");
    notification.setType("information");
    if(!dataService.updateObjectState(objet) && !dataServiceNotification.createOne(notification)) return null;

    return objet;
  }
  @Override
  public ObjetDTO venteObject(ObjetDTO objetDTO){


    LocalDate today = LocalDate.now();

    Objet objet=(Objet) objetDTO;

    objet.setDate_vente(today);

    NotificationImpl notification= (NotificationImpl) notifFactory.getNotification();
    notification.setObject(objetDTO.getIdObjet());
    notification.setMessage("l'objet n° : "+objetDTO.getIdObjet()+" a été mis en vente");
    notification.setType("information");
    if(!dataService.updateObjectState(objet) && !dataServiceNotification.createOne(notification)) return null;

    return objet;
  }
  @Override
  public ObjetDTO venduObject(ObjetDTO objetDTO){

    Objet objet=(Objet) objetDTO;

    NotificationImpl notification= (NotificationImpl) notifFactory.getNotification();
    notification.setObject(objetDTO.getIdObjet());
    notification.setMessage("l'objet n° : "+objetDTO.getIdObjet()+" a été vendu");
    notification.setType("information");
    if(!dataService.updateObjectState(objet) && !dataServiceNotification.createOne(notification)) return null;

    return objet;
  }
}
