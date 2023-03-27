// eslint-disable-next-line no-unused-vars
import { Navbar as BootstrapNavbar } from 'bootstrap';
import {getAuthenticatedUser,getToken} from '../../utils/auths';
import Navigate from "../Router/Navigate";

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
      <div style="display: flex; margin-left: 250px;">
        <h1>
          <span style="background-color: #eee; padding: 20px;" id="nomSite">
            DonneMoi
          </span>
        </h1>
      </div>
    </div>
    <div style="flex-grow: 1;" id='bouttonCo'>
    </div>
  </div>
  
`

  h.innerHTML = head;
}

async function renderNavbar() {
  const authenticatedUser = await getAuthenticatedUser();

  const navadd = `
  <div style=" justify-content: center; display: flex" >
    <nav class="navbar navbar-expand-lg navbar-light " style="background-color: #A47148; border: black solid 1px;">
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
              <a class="nav-link active" aria-current="page" href="#" data-uri="/">Objets en magasin</a>
            </li>      
            <li id="loginItem" class="nav-item">
              <a class="nav-link"  href="#" data-uri="/?etat=vente">Objets en vente</a>
            </li>
            <li id="registerItem" class="nav-item">
              <a class="nav-link"  href="#" data-uri="/?etat=vendu">Objets vendu </a>
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
    if(authenticatedUser?.role==='aidant' || authenticatedUser?.role==='responsable'){
    member.innerHTML+=`
    
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/Disponibilités">Disponibilités</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/ReceptionObjets">Dépôt d'objets</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/RechercheObjets">Gérer la vente d'objets</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/TableauDeBord">TableauDeBord</a>
    </li>
    `;
    }
    if(authenticatedUser?.role==='responsable'){
      
      member.innerHTML+=`
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/ObjetsPropose">Accepter/Refuser objets</a>
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
    
    <li class="nav-item">
              <a class="nav-link" >${authenticatedUser.prenom}</a>
    </li>
    `;
  }
  else{
    const bouttonCo = document.getElementById('bouttonCo');
    bouttonCo.innerHTML =`
    <a class="nav-link" href="/login" style="display: inline-block; background-color: grey; color: black; padding: 10px 20px; border-radius: 5px; text-decoration: none; float: right">Se Connecter</a>
    `;
    
    

  }
  const buttonTitle = document.querySelector("#nomSite");
  buttonTitle.addEventListener("click",()=>{
    Navigate("/")
  })
      
}

export default Navbar;
