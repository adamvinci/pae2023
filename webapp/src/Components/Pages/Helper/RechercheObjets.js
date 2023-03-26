import {clearPage} from "../../../utils/render";
import {getToken} from "../../../utils/auths";

const tableEnTete = `
  <div style=" justify-content: center; display: flex"><h1>Rechercher d'objet</h1></div>
  <div style=" justify-content: center; display: flex">
  
    <table class="tableEnTete">
      <thead> 
        <tr> 
          <th class="rechercheObjetsTh"> Type d'objet </th> 
          <th class="rechercheObjetsTh"> date de dépot </th>
          <th class="rechercheObjetsTh"> Prix de l'objet </th>
          <th class="rechercheObjetsTh"> Etat de vente </th>
          <th class="rechercheObjetsTh"> Photo objet </th>
          <th class="rechercheObjetsTh"> </th>
        </tr>
      </thead>
      <tbody class="tableData"> 
      </tbody>    
    </table>
  </div>`;

function head() {
  const main = document.querySelector('main');
  main.innerHTML += tableEnTete;
}
async function getTypeObject() {

  const options = {
    method: 'GET',
  };
  const response = await fetch(`${process.env.API_BASE_URL}/objet/typeObjet`,
      options);

  if (!response.ok) {
    throw new Error(
        `fetch error : ${response.status} : ${response.statusText}`);
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


function closePopup() {
  const popup = document.querySelector('.popUpContainer');
  if (popup) {
    popup.parentNode.removeChild(popup);
  }
}
function homeScreen(){
  let data;
  let dataHtml = ' ';
  async function getData() {
    try {
      const response = await fetch(`${process.env.API_BASE_URL}/objet`);

      if (!response.ok) {
        throw new Error('Network response was not ok.');
      }

      const datas = await response.json();
      data = datas;
      const size = data.length;

      const tableBody = document.querySelector('.tableData');
      for (let i = 0; i < size;) {
        dataHtml = `
    <tr style="font-family: 'Games', sans-serif;">
       <td class="rechercheObjetsTd">${data[i].typeObjet.libelle}</td> 
      
      <td class="rechercheObjetsTd">${data[i].date_depot}</td>
      <td class="rechercheObjetsTd" id="prixDonne">`;

        if (data[i].prix) {
          dataHtml += `${data[i].prix}€`;
        } else {
          dataHtml += `<input type="text" size="1" id="prix-${data[i].idObjet}">
                  <button class="confirmer" data-id="${data[i].idObjet}">Confirmer</button>`;
        }

        dataHtml += `</td>
      <td class="rechercheObjetsTd">`;
        if (data[i].etat === "en vente") {
          dataHtml += `<button  class="buttonVendu" size="1" data-index="${data[i].idObjet}" style="background-color: indianred">Indiquer vendu</button>`;
        }else{
          dataHtml += `<p>${data[i].etat}</p>`;
        }
        dataHtml += `</td> 

      <td class="td"><img src=/api/objet/getPicture/${data[i].idObjet} alt="photo" width="100px"></td> 
      <td class="rechercheObjetsTd"><input type="button" class="btn btn-info btn-sm" id="details" value="Détails"></td>
  </tr>`;
        i+=1;

        tableBody.innerHTML += dataHtml;
      }

      const venduBtns = document.querySelectorAll('.buttonVendu');
      venduBtns.forEach(btn => {
        btn.addEventListener("click",async (event)=> {
          const val = event.target.getAttribute('data-index');
          try{
            const options = {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                Authorization : getToken()
              },
            };

            const rep = await fetch(`${process.env.API_BASE_URL}/objet/vendreObject/${val}`, options);

            if (!rep.ok) throw new Error(`fetch error : ${rep.status} : ${rep.statusText}`);
            window.location.reload();
          }
          catch (err){
            throw Error(err);
          }

        });
      });



      const confirmerBtns = document.querySelectorAll('.confirmer');
      confirmerBtns.forEach(btn => {
        btn.addEventListener('click', async (event) => {
          const idObjet = event.target.dataset.id;
          const prixInput = document.querySelector(`#prix-${idObjet}`);
          const prix = prixInput.value;
          try{
            const options = {
              method: 'POST',
              body: JSON.stringify({
                prix,
              }),
              headers: {
                'Content-Type': 'application/json',
                Authorization : getToken()
              },
            };


            const rep = await fetch(`${process.env.API_BASE_URL}/objet/misEnVenteObject/${idObjet}`, options);

            if (!rep.ok) throw new Error(`fetch error : ${rep.status} : ${rep.statusText}`);
            window.location.reload();
          }
          catch (err){
            throw Error(err);
          }
        });
      });
      const buttonInfo = document.querySelectorAll('#details');
      buttonInfo.forEach((button) => {
        button.addEventListener("click", event => {
          event.preventDefault();
          const objTr = event.target.closest("tr");
          const objIndex = Array.from(objTr.parentNode.children).indexOf(objTr);
          const objData = data[objIndex];
          const main = document.querySelector("main");
          const popUp = `
      <div class="popUpContainer">
        <div id="informationContainer">
          <div class="photo"> 
            <h1>Photo</h1>
            <img src="/api/objet/getPicture/${objData.idObjet}" alt="Photo" width="100px">
          </div>
          <div class="description">
            <h1>Description</h1>
            <p>${objData.description}</p>
          </div>
          <div class = "detail">
           <table class="tableEnTete">
              <thead> 
                <tr> 
                  <th class="rechercheObjetsTh"> Localisation </th> 
                  <th class="rechercheObjetsTh"> Type d'objet </th> 
                  <th class="rechercheObjetsTh"> Date dépot </th>
                  <th class="rechercheObjetsTh"> Date vente </th>
                  <th class="rechercheObjetsTh"> Prix de l'objet </th>
                  <th class="rechercheObjetsTh"> Etat de vente </th>
                  <th class="rechercheObjetsTh"> Proposes par </th>
                </tr>
              </thead>
              <tbody class="tableData">
                <tr style="font-family: 'Games', sans-serif;">
                  <td class="rechercheObjetsTd">${objData.localisation}</td> 
                  <td class="rechercheObjetsTd">${objData.typeObjet.libelle}</td>
                  <td class="rechercheObjetsTd">${objData.date_depot}</td>
                  <td class="rechercheObjetsTd">${objData.date_vente}</td>
                  <td class="rechercheObjetsTd">${objData.prix} € </td>
                  <td class="rechercheObjetsTd">${objData.etat}</td>
                  <td class="rechercheObjetsTd">/</td>
                </tr>
              </tbody>    
            </table>
          </div>
          <div class = "voirPlusObjetUtilisateur"></div>
          <input type="button" id="closeButton" value="Fermer">
        </div>
       
      </div>
    `;
          main.insertAdjacentHTML("beforeend", popUp);

          const popupContainer = document.querySelector('.popUpContainer');
          const closeButton = popupContainer.querySelector('#closeButton');
          closeButton.addEventListener('click', closePopup);
        });
      })

    } catch (error) {
      throw new Error(error);
    }
  }
  getData();
}


const RechercheObjets =async () =>  {
  clearPage();
  const main = document.querySelector('main');
  const filterRechercheObjet =await filtrageObjet();
  main.innerHTML += filterRechercheObjet;
  head();
  homeScreen();
  };

export default RechercheObjets;