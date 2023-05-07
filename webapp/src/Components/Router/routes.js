import HomePage from '../Pages/HomePage';
import Logout from '../Logout/Logout';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import Disponibilites from '../Pages/Helper/Disponibilites';
import ReceptionObjets from '../Pages/Helper/ReceptionObjets';
import ListeObjets from '../Pages/Helper/ListeObjets';
import VenteDobjet from '../Pages/Helper/VenteDobjet';
import Membres from '../Pages/Responsable/Membres';
import ObjetsPropose from '../Pages/Responsable/ObjetsPropose';
import AddObjetPage from "../Pages/AddObjetPage";
import UserPage from "../Pages/UserPage";
import MyObjectPage from "../Pages/MyObjectPage";
import UserObjectPage from "../Pages/Helper/UserObjectPage";
import UpdateObjectPage from '../Pages/Helper/UpdateObjet';
import tableauDeBord from "../Pages/Helper/TableauDeBord";



const routes = {
  '/': HomePage,
  '/?etat=vente': HomePage,
  '/?etat=vendu': HomePage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': Logout,
  '/Disponibilites': Disponibilites,
  '/ReceptionObjets': ReceptionObjets,
  '/TableauDeBord': tableauDeBord,
  '/venteObjet': VenteDobjet,
  '/Membres': Membres,
  '/ObjetsPropose': ObjetsPropose,
  '/ListeObjets' : ListeObjets,
  '/AddObjet': AddObjetPage,
  '/UserPage': UserPage,
  '/MyObjectPage': MyObjectPage,
  '/UserObjectPage': UserObjectPage,
  "/updatePage": UpdateObjectPage,

  
};

export default routes;
