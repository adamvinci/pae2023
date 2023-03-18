package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.Objet;
import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.dto.TypeObjetDTO;
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
  public Boolean accepterObjet(ObjetDTO objetDTO){

    LocalDate today = LocalDate.now();

    Objet objet=(Objet) objetDTO;

    objet.setDate_acceptation(today);


    return dataService.updateObjectState(objetDTO);
  }
  @Override
  public Boolean refuserObject(ObjetDTO objetDTO){

    Objet objet=(Objet) objetDTO;


    return dataService.updateObjectState(objetDTO);
  }
  @Override
  public Boolean depotObject(ObjetDTO objetDTO){


    LocalDate today = LocalDate.now();
    Objet objet=(Objet) objetDTO;

    objet.setDate_depot(today);



    return dataService.updateObjectState(objetDTO);
  }
  @Override
  public Boolean venteObject(ObjetDTO objetDTO){


    LocalDate today = LocalDate.now();

    Objet objet=(Objet) objetDTO;

    objet.setDate_vente(today);


    return dataService.updateObjectState(objetDTO);
  }
  @Override
  public Boolean venduObject(ObjetDTO objetDTO){

    Objet objet=(Objet) objetDTO;

    return dataService.updateObjectState(objetDTO);
  }
}
