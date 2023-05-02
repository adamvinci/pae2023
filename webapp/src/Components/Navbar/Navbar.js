// eslint-disable-next-line no-unused-vars
import { Navbar as BootstrapNavbar } from 'bootstrap'; // NE PAS SUPPRIMER !!!
import {getAuthenticatedUser,getToken} from '../../utils/auths';
import Navigate from '../Router/Navigate';

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
      <div class="logoo"">
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
          <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            Objets
          </a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#" data-uri="/">Objets en magasins</a></li>
            <li><a class="dropdown-item" href="#" data-uri="/?etat=vendu">Objets en vente</a></li>
            <li><a class="dropdown-item" href="#"  data-uri="/?etat=vente">Objets vendus</a></li>
          </ul>
        </li> 
              <li id="registerItem" class="nav-item">
              <a class="nav-link"  href="#" data-uri="/AddObjet">Proposer un objet </a>
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
              <a class="nav-link" href="#" data-uri="/Disponibilites">Disponibilités</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/ReceptionObjets">Dépôt d'objets</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/TableauDeBord">Tableau de bord</a>
    </li>
      <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/Statistiques">Statistiques</a>
    </li>
    <li class="nav-item">
              <a class="nav-link" href="#" data-uri="/venteObjet">Gérer la vente d'objets</a>
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
      <a class="nav-link" href="#" data-uri="/MyObjectPage">Mes objets</a>
    </li>
    <li id="registerItem" class="nav-item">
    <button id = "profileButton" type="button" class="btn btn-outline-secondary">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="" class="bi bi-person-circle" viewBox="0 0 16 16">
        <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"></path>
        <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"></path>
      </svg>
    <span class="visually-hidden">Button</span>
  </button>
    </li>  
    <li class="nav-item">
        <button id="logoutButton" type="button" class="btn btn-outline-secondary">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="" class="bi bi-arrow-right-square" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M15 2a1 1 0 0 0-1-1H2a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V2zM0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V2zm4.5 5.5a.5.5 0 0 0 0 1h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5H4.5z"></path>
        </svg>
        <span class="visually-hidden">Button</span>
      </button>
    </li>
    <li class="nav-item">
              <a class="nav-link" >${authenticatedUser.prenom}</a>
    </li>
    

    `;
    let notifications=[];
    const boutNotif =()=> {

      const nonLues = notifications.filter(notification => !notification.lue);
      let nbNonLues = nonLues.length;
      const bouttonCo = document.getElementById('bouttonCo');
      bouttonCo.innerHTML = `
        <div class="notification-container" style="position: relative; z-index: 1;">
          <button class="notification-button" id="notification-btn">
            <span class="notification-icon"><i class="fa fa-bell"></i></span>
            <span class="notification-text">Notification</span>
            <span class="notification-count" id="notification-count">${nbNonLues}</span>
          </button>
          <div class="notification-dropdown" id="notification-dropdown">
          <ul id="notification-list"></ul>
          </div>
        </div>  
        `;

      const notificationBtn = document.getElementById("notification-btn");
      const notificationDropdown = document.getElementById("notification-dropdown");
      const notificationList = document.getElementById("notification-list");

      notificationBtn.addEventListener("click", async () => {
        notificationDropdown.classList.toggle("show");

        if (notificationDropdown.classList.contains("show")) {

          notificationList.innerHTML = notifications.map(notification => {
            const isRead = notification.lue;
            const classList = isRead ? "notification-item read" : "notification-item unread";
            return `<li class="${classList}" data-notification-id="${notification.id}">${notification.message}</li>`;
          }).join('');

          // Appel API pour marquer la notification comme lue lorsque l'utilisateur clique dessus
          const notificationItems = document.querySelectorAll(".notification-item");

          notificationItems.forEach((item) => {
            item.addEventListener("click", async () => {
              if (!item.classList.contains("read")) {
                const {dataset: {notificationId}} = item;
                const options = {
                  method: 'POST',
                  body: JSON.stringify({}),
                  headers: {
                    'Content-Type': 'application/json',
                    Authorization : getToken(),
                  },
                };


                const response = await fetch(`${process.env.API_BASE_URL}/notification/marquerRead/${notificationId}`, options);

                const updatedNotification = await response.json();
                if (updatedNotification != null) {
                  const notificationIndex = notifications.findIndex(notification => notification.id === updatedNotification.id);
                  if (notificationIndex !== -1) {
                    notifications[notificationIndex].lue = true;
                  }
                  const notificationCount = document.getElementById("notification-count");
                  nbNonLues-=1;
                  notificationCount.innerText = nbNonLues;
                  item.classList.remove("unread");
                  item.classList.add("read");

                }
              }
            });
          });
        }
      });
    }
    const recupererMessages= async()=>{
      const optionsGET = {
        method: 'GET',
        body: JSON.stringify(),
        headers: {
          'Content-Type': 'application/json',
          Authorization: getToken(),
        },
      };
      const responsee = await fetch(`${process.env.API_BASE_URL}/notification/userNotifications`,optionsGET);
      notifications = await responsee.json();
      boutNotif();
    }
    recupererMessages();

  }
  else{
    const bouttonCo = document.getElementById('bouttonCo');
    bouttonCo.innerHTML =`
    <a class="nav-link" href="/login" style="display: inline-block; background-color: grey; color: black; padding: 10px 20px; border-radius: 5px; text-decoration: none; float: right">Se Connecter</a>
    `;
    
    

  }
  const buttonTitle = document.querySelector("#nomSite");
  buttonTitle.addEventListener("click",()=>{
    Navigate('/');;
  })

    const logoutButton = document.querySelector("#logoutButton");
    if(logoutButton) {
      logoutButton.addEventListener("click", () => {
        Navigate('/logout');
      })
    }

  const profileButton = document.querySelector("#profileButton");
  if(profileButton){
    profileButton.addEventListener("click",()=>{
      Navigate('/UserPage');
    })
  }
      
}

export default Navbar;
