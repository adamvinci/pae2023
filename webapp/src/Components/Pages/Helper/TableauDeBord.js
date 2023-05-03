import Swal from "sweetalert2";
import {clearPage} from "../../../utils/render";
import {getToken} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";


const tableEnTete = `

  <div id = "tableData">
  <div>
  
      <h1 >Tableau De Bord</h1>
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
  let optionsHtml;
  const typeObjet = await getTypeObject();
  optionsHtml = '';
  for (let i = 0; i < typeObjet.length;) {
    const type = typeObjet[i];

    optionsHtml += `
      <div>
        <input type="checkbox" id="${type.idObjet}" name="type" value="${type.libelle}">
        <label for="${type.idObjet}">${type.libelle}</label>
      </div>
    `;
    i += 1;
  }
  const filtre = `

    <div class="filterRechercheObjet">
      <form>
        <div>
          <h3 class="">Type d'objet</h3>
          ${optionsHtml}
        </div>
        <p>
          <h3>Prix</h3>
          <input type="text" id="prix1" size="1"> jusqu'a <input type="text" id="prix2" size="1">
        </p>
        <p>
          <h3>Disponibilite</h3>
          <input type="date" id="dateDebut"> <p>jusqu'au </p>
          <input type="date" id="dateFin">
        </p>
        <p></p>
        <input type="button" id="filtreBtn" value="Filtrer">
        
      </form>
    </div>
  `;
  return filtre;
}

async function objetFiltrer() {

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

  const typesSelectionnes = document.querySelectorAll(
      'input[type="checkbox"][name="type"]:checked');
  const typesValeurs = [];
  typesSelectionnes.forEach(type => {
    typesValeurs.push(type.value);
  });

  const prix1 = document.getElementById("prix1").value;
  const prix2 = document.getElementById("prix2").value;
  const dateDebut = document.getElementById("dateDebut").value;
  let dateFin = document.getElementById("dateFin").value;


  let [annee, mois, jour] = dateFin.split("-");
  annee = parseInt(annee, 10);
  mois = parseInt(mois, 10);
  jour = parseInt(jour, 10);
  dateFin = `${annee}-${mois}-${jour}`;



  console.log()

  const data = datas.filter(d =>
      (d.prix >= prix1 && d.prix <= prix2)
      || typesValeurs.includes(d.typeObjet.libelle)
      || (d.date_acceptation && `${d.date_acceptation[0]}-${d.date_acceptation[1]}-${d.date_acceptation[2]}` >= dateDebut && `${d.date_acceptation[0]}-${d.date_acceptation[1]}-${d.date_acceptation[2]}` <= dateFin)
  );



  console.log(data);

  tableAllObject(data);
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

  tableAllObject(datas);
}

async function tableAllObject(datas) {
  const tableBody = document.querySelector('.tableData');
  // Efface la table avant de l'afficher à nouveau
  tableBody.innerHTML = '';
  console.log(datas);
  datas.forEach((objet) => {
    tableBody.innerHTML += `
    <tr data-id="${objet.idObjet}">
      <td class="receptionObjetsTd">${objet.idObjet}</td> 
      <td class="receptionObjetsTd">${objet.typeObjet.libelle}</td>
      <td class="td"><img src=/api/objet/getPicture/${objet.idObjet} alt="photo" width="100px"></td> 
      <td class="receptionObjetsTd">${objet.description}</td>
      <td class="receptionObjetsTd">${objet.etat ? changeEtatName(objet.etat)
        : '/'}</td>
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
      <td class="receptionObjetsTd"> ${objet.utilisateur ?`<a id = "buttonDetail" data-id=${objet.utilisateur}>Details Utilisateur</a> `: objet.gsm
         }</td>
      <td class="receptionObjetsTd"><button id="modifier" class="btn btn-secondary" type="button">Modifier l'object</button></td>
    </tr>`;
  })

  const btnUpdate = document.querySelectorAll("#modifier");
  btnUpdate.forEach((button) => {
    button.addEventListener("click", () => {
      const id = parseInt(button.closest('tr').getAttribute("data-id"), 10);

      const objectSelectionner = datas.find(element => element.idObjet === id);

      sessionStorage.setItem("objet", JSON.stringify(objectSelectionner));

      window.location.href = "/updatePage";
    });
  });

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
      const response2 = await fetch(
          `${process.env.API_BASE_URL}/users/${idUser}`, options);
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
      </div>`

      const main = document.querySelector('main');
      main.insertAdjacentHTML("beforeend", popup);
      const popupContainer = document.querySelector('.popUpContainer');
      const closeButton = popupContainer.querySelector('#closeButtonTb');
      const userObjetButton = popupContainer.querySelector(
          '#objectUserButtonTb');
      closeButton.addEventListener('click', closePopup);
      userObjetButton.addEventListener('click', navigateUserPage)
    })
  })
}

function navigateUserPage() {
  const idUs = parseInt(document.getElementById('idUser').textContent, 10);
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

async function head() {

  const main = document.querySelector('main');
  let html = "<div id=pageTb>"
  html += await filtrageObjet();
  html += tableEnTete;
  table();

  html += "</div>"
  main.innerHTML = html;

  const filtreBtn = document.getElementById("filtreBtn");
  filtreBtn.addEventListener("click", objetFiltrer);

}

const TableauDeBord = async () => {
  clearPage();
  await head();
};

export default TableauDeBord;