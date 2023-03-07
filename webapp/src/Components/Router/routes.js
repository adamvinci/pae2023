import HomePage from '../Pages/HomePage';
import Logout from '../Logout/Logout';
import LoginPage from '../Pages/LoginPage';
import RegisterPage from '../Pages/RegisterPage';
import ObjetsEnVente from '../Pages/ObjetEnVentePage';
import ObjetsVendu from '../Pages/ObjetVenduPage';
import ObjetsProposer from '../Pages/ObjetProposerPage';
import UsersPage from '../Pages/UsersPage';

const routes = {
  '/': HomePage,
  '/login': LoginPage,
  '/register': RegisterPage,
  '/logout': Logout,
  '/ObjetEnVente': ObjetsEnVente,
  '/ObjetVendu': ObjetsVendu,
  '/ObjetProposer': ObjetsProposer,
  '/users' : UsersPage
};

export default routes;
