import Swal from "sweetalert2";
import {clearPage} from "../../../utils/render";
import {getToken} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";

const tableEnTete = `

  <div id = "tableData">
  <div>
  
      <h1 >Réception d'objets</h1>
</div>
    <table class="tableEnTete">
      <thead> 
        <tr> 
          <th class="receptionObjetsTh"> Id Objet </th> 
          <th class="receptionObjetsTh"> Type d'objet </th>
          <th class="receptionObjetsTh"> Photo objet </th>
          <th class="receptionObjetsTh"> Description </th>
           <th class="receptionObjetsTh"> Etat </th>
           <th class="receptionObjetsTh"> Date d'acceptation </th>
          <th class="receptionObjetsTh"> Date de depot </th>
          <th class="receptionObjetsTh"> Localisation </th>
          <th class="receptionObjetsTh"> Prix </th>
          <th class="receptionObjetsTh"> Date de vente </th>
                    <th class="receptionObjetsTh"> Utilisateur </th>
        </tr>
      </thead>
      <tbody class="tableData"> 
      </tbody>    
    </table>
  </div>`;



async function getTypeObject() {

  const options = {
    method: 'GET',
  };
  const response = await fetch(`${process.env.API_BASE_URL}/objet/typeObjet`,
      options);

  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  const objets = await response.json();
  return objets;
}

async function filtrageObjet() {
  const typeObjet = await getTypeObject();

  let type = "";
  type += `<h1>Type d'objet</h1>`;
  typeObjet.forEach((typee) => {
    type += `
      <div id="typeObjet">
        <label>
          <input type="checkbox" name="menu" data-value="${typee.libelle}">
          ${typee.libelle}
        </label>
      </div>
    `;
  });

  return `
    <div class="filterRechercheObjet">
      <form>
        ${type}
        <p>
          <h3>Prix</h3>
          $<input type="text" size="1"> jusqu'a <input type="text" size="1">
        </p>
        <p>
          <h3>Date depot</h3>
          <input type="date" id="date"><br>
        </p>
        <p></p>
        <input type="button" value="Filtrer">
      </form>
    </div>
  `;
}

async function table() {

  const opt = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: getToken()
    },
  };
  const response = await fetch(`${process.env.API_BASE_URL}/objet`, opt);

  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  const datas = await response.json();


  const tableBody = document.querySelector('.tableData');
  datas.forEach((objet) => {
    tableBody.innerHTML += `
    <tr>
      <td class="receptionObjetsTd">${objet.idObjet}</td> 
      <td class="receptionObjetsTd">${objet.typeObjet.libelle}</td>
      <td class="td"><img src=/api/objet/getPicture/${objet.idObjet} alt="photo" width="100px"></td> 
      <td class="receptionObjetsTd">${objet.description}</td>
            <td class="receptionObjetsTd">${objet.etat ? changeEtatName(
        objet.etat) : '/'}</td>
      <td class="receptionObjetsTd">${objet.date_acceptation
        ? ` ${objet.date_acceptation[2]}/${objet.date_acceptation[1]}/${objet.date_acceptation[0]}`
        : '/'}</td>
          <td class="receptionObjetsTd">${objet.date_depot
        ? ` ${objet.date_depot[2]}/${objet.date_depot[1]}/${objet.date_depot[0]}`
        : '/'}</td>
      <td class="receptionObjetsTd"> ${objet.localisation ? objet.localisation
        : '/'}</td>
      <td class="receptionObjetsTd">${objet.prix ? objet.prix : '/'}</td>
      <td class="receptionObjetsTd">${objet.date_vente
        ? ` ${objet.date_vente[2]}/${objet.date_vente[1]}/${objet.date_vente[0]}`
        : '/'}</td>
 <td class="receptionObjetsTd"> ${objet.gsm ? objet.gsm
        : `<a id = "buttonDetail" data-id=${objet.utilisateur}>Details Utilisateur</a> `}</td>
    </tr>`;
  })
  const btnDetail = document.querySelectorAll("#buttonDetail");
  let popup;
  btnDetail.forEach((btn) => {
    btn.addEventListener('click', async (e) => {

      const idUser = e.target.dataset.id;
      const options = {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: getToken()
        },
      };

      const response1 = await fetch(
          `${process.env.API_BASE_URL}/auths/getPicture/${idUser}`, options);

      const img1 = await response1.blob();
      const response2 = await fetch(`${process.env.API_BASE_URL}/users/${idUser}`,
          options);
      const users = await response2.json();
      popup = ` 
 <div class="popUpContainer">
        <div id="informationContainerTb" style="overflow-x: scroll;">
          <div class="photoPopup"> 
            <img src=${URL.createObjectURL(img1)}  alt="Photo" >
          </div>
     
          <div class = "detail">
           <table>
              <thead> 
                <tr> 
                 <th class="rechercheObjetsTh"> Nom </th> 
                  <th class="rechercheObjetsTh"> Prenom </th> 
                  <th class="rechercheObjetsTh"> email  </th>
                  <th class="rechercheObjetsTh"> Date inscription </th>
                  <th class="rechercheObjetsTh"> Role </th>
                                    <th class="rechercheObjetsTh"> Gsm </th>
                </tr>
              </thead>
              <tbody class="tableData">
                <tr>
                               <td class="rechercheObjetsTd">${users.nom}</td> 
               <td class="rechercheObjetsTd">${users.prenom}</td> 
                  <td class="rechercheObjetsTd">${users.email}</td> 
            <td class="rechercheObjetsTd"> Le ${users.dateInscription[2]}/${users.dateInscription[1]}/${users.dateInscription[0]}</td>
                  <td class="rechercheObjetsTd">${users.role}</td>
                                    <td class="rechercheObjetsTd">${users.gsm}</td>
                                     <span id="idUser" style="display: none;">${users.id}</span>
                </tr>
              </tbody>    
            </table>
          </div>
          <div class = "fermer"></div>
          <input type="button" id="closeButtonTb" value="Fermer">
          <div class = "objetUser"></div>
          <input type="button" id="objectUserButtonTb" value="voir les objets de l'user">
        </div>
       
      </div>

    `
      const main = document.querySelector('main');
      main.insertAdjacentHTML("beforeend", popup);
      const popupContainer = document.querySelector('.popUpContainer');
      const closeButton = popupContainer.querySelector('#closeButtonTb');
      const userObjetButton = popupContainer.querySelector('#objectUserButtonTb');
      closeButton.addEventListener('click', closePopup);
      userObjetButton.addEventListener('click', navigateUserPage)
    })
  })

}
function navigateUserPage(){
  const idUs = parseInt(document.getElementById('idUser').textContent,10);
  console.log(idUs);
  Navigate(`/UserObjectPage?idUs=${idUs}`);

}
function closePopup() {
  const popup = document.querySelector('.popUpContainer');
  if (popup) {

    popup.parentNode.removeChild(popup);
  }

}

function changeEtatName(etat) {
  if (etat === 'accepte') {
    return 'Accepté'
  }
  if (etat === 'refuser') {
    return 'Refusé'
  }
  if (etat === 'en vente') {
    return 'En Vente'
  }
  if (etat === 'vendu') {
    return 'Vendu'
  }
  return 'Proposé'

}

const TableauDeBord = async () => {
  clearPage();
  const main = document.querySelector('main');
  let html = "<div id=pageTb>"

  html += await filtrageObjet();
  html += tableEnTete;
  table();
  html +="</div>"
  main.innerHTML =html


};

export default TableauDeBord;