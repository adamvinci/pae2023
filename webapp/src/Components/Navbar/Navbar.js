// eslint-disable-next-line no-unused-vars
import { Navbar as BootstrapNavbar } from 'bootstrap';
import {getAuthenticatedUser,getToken} from '../../utils/auths';


const Navbar = () => {
  header();
  renderNavbar();
};

function header(){

  const h = document.querySelector('#head');
  const head = `
  <div style="display: flex; flex-wrap: wrap; height: 10vh;">
    <div style="flex-grow: 1;">
      <div style="display: flex;">
        <span style="background-color: #eee; padding: 20px;">
          <p>Rue de Heuseux 77ter<br>4671 BLEGNY</p>
        </span>
      </div>
    </div>
    <div style="flex-grow: 1;">
      <div style="display: flex; margin-left: 235px;">
        <h1>
          <span style="background-color: #eee; padding: 20px;">
            DonneMoi
          </span>
        </h1>
      </div>
    </div>
    <div style="flex-grow: 1;"></div>
  </div>
  
`

  h.innerHTML = head;
}

async function renderNavbar() {
  const authenticatedUser = await getAuthenticatedUser();

  const navadd = `
  <div style=" justify-content: center; display: flex" >
    <nav class="navbar navbar-expand-lg navbar-light " style="background-color: mediumorchid; border-color: green; border-style: solid">
      <div class="container-fluid">
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0" id='member'>
            <li class="nav-item">
              <a class="nav-link active" aria-current="page" href="#" data-uri="/ObjetEnVente">Objets mis en vente</a>
            </li>      
            <li id="loginItem" class="nav-item">
              <a class="nav-link" href="#" data-uri="/ObjetVendu">Objets vendus</a>
            </li>
            <li id="registerItem" class="nav-item">
              <a class="nav-link" href="#" data-uri="/ObjetProposer">Objets à proposer</a>
            </li>      
            <li id="registerItem" class="nav-item">
              <a class="nav-link" href="#" data-uri="/register">register</a>
            </li>     
             <li id="registerItem" class="nav-item">
              <a class="nav-link" href="#" data-uri="/login">Login</a>
            </li> 
            
          </ul>
          
        </div>
      </div>
    </nav>
  </div>
`;

  const navbar = document.querySelector('#navbarWrapper');

  navbar.innerHTML = navadd;

  if(getToken()){
    const member=document.getElementById('member');
    console.log(authenticatedUser?.role);
    if(authenticatedUser?.role==='aidant' || authenticatedUser?.role==='Responsable'){
    member.innerHTML+=`
    
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/Disponibilités">Disponibilités</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/ReceptionObjets">ReceptionObjets</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/RechercheObjets">RechercheObjets</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/TableauDeBord">TableauDeBord</a>
    </li>
    `;
    }
    if(authenticatedUser?.role==='Responsable'){
      
      member.innerHTML+=`
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/ObjetsPropose">ObjetsPropose</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/Membres">Membres</a>
    </li>
    `;
    }
    member.innerHTML+=`
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/logout">Logout</a>
    </li>
    `;
  }
      
}

export default Navbar;
