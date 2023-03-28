import HomePage from '../Pages/HomePage';
import Logout from '../Logout/Logout';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';

import ObjetsProposer from '../Pages/ObjetProposerPage';
import Disponibilités from '../Pages/Helper/Disponibilités';
import ReceptionObjets from '../Pages/Helper/ReceptionObjets';
import TableauDeBord from '../Pages/Helper/TableauDeBord';
import VenteDobjet from '../Pages/Helper/VenteDobjet';
import Membres from '../Pages/Responsable/Membres';
import ObjetsPropose from '../Pages/Responsable/ObjetsPropose';
import UserPage from "../Pages/UserPage";


const routes = {
  '/': HomePage,
  '/?etat=vente': HomePage,
  '/?etat=vendu': HomePage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': Logout,
  '/ObjetProposer': ObjetsProposer,
  '/Disponibilités': Disponibilités,
  '/ReceptionObjets': ReceptionObjets,
  '/TableauDeBord': TableauDeBord,
  '/venteObjet': VenteDobjet,
  '/Membres': Membres,
  '/ObjetsPropose': ObjetsPropose,
  '/UserPage': UserPage,
};

export default routes;
