import {clearPage} from "../../../utils/render";

const tableEnTete = `
  <div style=" justify-content: center; display: flex"><h1>Rechercher d'objet</h1></div>
  <div style=" justify-content: center; display: flex">
  
    <table class="tableEnTete">
      <thead> 
        <tr> 
          <th class="rechercheObjetsTh"> Type d'objet </th> 
          <th class="rechercheObjetsTh"> date de dépot </th>
          <th class="rechercheObjetsTh"> Prix de l'objet </th>
          <th class="rechercheObjetsTh"> Nouveau prix </th>
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

const filterRechercheObjet = `
  <div class="filterRechercheObjet">
    <form>
      <h3>Types d'objet</h3>
      <label>
        <input type="checkbox" name="Meuble" value="Meuble">
        Meuble
      </label>
      <br>
      <label>
        <input type="checkbox" name="Table" value="Table">
        Table
      </label>
      <br>
      <label>
        <input type="checkbox" name="Chaise" value="Chaise">
        Chaise
      </label>
      <br>
      <label>
        <input type="checkbox" name="Fauteuil" value="Fauteuil">
        Fauteuil
      </label>
      <br>
      <label>
        <input type="checkbox" name="Lit/sommier" value="Lit/sommier">
        Lit/sommier
      </label>
      <br>
      <label>
        <input type="checkbox" name="Matelas" value="Matelas">
        Matelas
      </label>
      <br>
      <label>
        <input type="checkbox" name="Couvertures" value="Couvertures">
        Couvertures
      </label>
      <br>
      <label>
        <input type="checkbox" name="Materiel de cuisine" value="Materiel de cuisine">
        Materiel de cuisine
      </label>
      <br>
      <label>
        <input type="checkbox" name="Vaisselle" value="Vaisselle">
        Vaisselle
      </label>
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
`

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

      for (let i = 0; i < size;) {
        dataHtml += `
        <tr style="font-family: 'Games', sans-serif;">
          <td class="rechercheObjetsTd">${data[i].type}</td> 
          <td class="rechercheObjetsTd">${data[i].date_depot}</td>
          <td class="rechercheObjetsTd">${data[i].prix} €</td>
          <td class="rechercheObjetsTd"><input type="text" size="1" > <input type="button" value="confirmer" </td>
          <td class="rechercheObjetsTd">
            <select>
              <option value="" disabled selected>Type Objet</option>
              <option value="vendu">Vendu</option>
            </select> 
          </td>  
          <td class="td"><img src=/api/objet/getPicture/${data[i].idObjet} alt="photo" width="100px"></td> 
          <td class="rechercheObjetsTd"><input type="button" class="btn btn-info btn-sm" id="details" value="Détails"></td>
         </tr>`;
        i += 1
      }

      const tableBody = document.querySelector('.tableData');
      tableBody.innerHTML += dataHtml;
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
                  <td class="rechercheObjetsTd">${objData.type}</td>
                  <td class="rechercheObjetsTd">${objData.date_depot}</td>
                  <td class="rechercheObjetsTd">${objData.date_vente}</td>
                  <td class="rechercheObjetsTd">${objData.prix_vente}</td>
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

function filtreObjet(){
  const main = document.querySelector('main');
  main.innerHTML += filterRechercheObjet;

}

const RechercheObjets = () => {
  clearPage();
  filtreObjet();
  head();
  homeScreen();
  };
  
export default RechercheObjets;