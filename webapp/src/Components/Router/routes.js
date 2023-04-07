import HomePage from '../Pages/HomePage';
import Logout from '../Logout/Logout';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';

import ObjetsProposer from '../Pages/ObjetProposerPage';
import Disponibilites from '../Pages/Helper/Disponibilites';
import ReceptionObjets from '../Pages/Helper/ReceptionObjets';
import TableauDeBord from '../Pages/Helper/TableauDeBord';
import VenteDobjet from '../Pages/Helper/VenteDobjet';
import Membres from '../Pages/Responsable/Membres';
import ObjetsPropose from '../Pages/Responsable/ObjetsPropose';

const routes = {
  '/': HomePage,
  '/?etat=vente': HomePage,
  '/?etat=vendu': HomePage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': Logout,
  '/ObjetProposer': ObjetsProposer,
  '/Disponibilites': Disponibilites,
  '/ReceptionObjets': ReceptionObjets,
  '/TableauDeBord': TableauDeBord,
  '/venteObjet': VenteDobjet,
  '/Membres': Membres,
  '/ObjetsPropose': ObjetsPropose,
};

export default routes;
