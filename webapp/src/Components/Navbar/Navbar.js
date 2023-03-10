// eslint-disable-next-line no-unused-vars
import { Navbar as BootstrapNavbar } from 'bootstrap';
import {getAuthenticatedUser,getToken} from '../../utils/auths';


const Navbar = () => {
  header();
  renderNavbar();
};

function header(){

  const h = document.querySelector('#head');
  const head = `<div style="display: flex; justify-content: space-between;">
    <div style="flex: 1; margin-top: 20px; float: left;">
      <p>Rue de Heuseux 77ter<br>4671 BLEGNY</p>
    </div>
    <div style="flex: 50%; margin-top: 20px;   font-size: 50px">
      <p>DonneMoi </p>
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
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
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
          <div id="member"></div>
        </div>
      </div>
    </nav>
  </div>
`;



  const navbar = document.querySelector('#navbarWrapper');

  

  navbar.innerHTML = navadd;
  if(!getToken()){
    
    navbar.innerHTML = navadd;
    console.log("shesh");
  }else{
    console.log(authenticatedUser?.nom);
    const member=document.getElementById('member');
    member.innerHTML=`
    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/logout">Logout</a>
    </li>
    </ul>
    `;
    if(authenticatedUser?.role==='aidant' || authenticatedUser?.role==='responsable'){
      member.innerHTML+=`
    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/logout">Aidant</a>
    </li>
    </ul>
    `;
    }
    if(authenticatedUser?.role==='responsable'){
      member.innerHTML+=`
    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/logout">Responsable</a>
    </li>
    </ul>
    `;
    }
   

  }
      
  
  // navbar.innerHTML = getToken() ? authenticatedUserNavbar : anonymousUserNavbar;
  // <li class="nav-item"><a class="nav-link disabled" href="#">${authenticatedUser?.nom}</a></li>
}

export default Navbar;
