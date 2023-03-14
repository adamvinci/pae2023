import HomePage from '../Pages/HomePage';
import Logout from '../Logout/Logout';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import ObjetsEnVente from '../Pages/ObjetEnVentePage';
import ObjetsVendu from '../Pages/ObjetVenduPage';
import ObjetsProposer from '../Pages/ObjetProposerPage';
import Disponibilités from '../Pages/Helper/Disponibilités';
import ReceptionObjets from '../Pages/Helper/ReceptionObjets';
import RechercheObjets from '../Pages/Helper/RechercheObjets';
import TableauDeBord from '../Pages/Helper/TableauDeBord';
import Membres from '../Pages/Responsable/Membres';
import ObjetsPropose from '../Pages/Responsable/ObjetsPropose';

const routes = {
  '/': HomePage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': Logout,
  '/ObjetEnVente': ObjetsEnVente,
  '/ObjetVendu': ObjetsVendu,
  '/ObjetProposer': ObjetsProposer,
  '/Disponibilités': Disponibilités,
  '/ReceptionObjets': ReceptionObjets,
  '/RechercheObjets': RechercheObjets,
  '/TableauDeBord': TableauDeBord,
  '/Membres': Membres,
  '/ObjetsPropose': ObjetsPropose,
};

export default routes;
