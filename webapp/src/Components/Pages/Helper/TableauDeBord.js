import Swal from "sweetalert2";
import {clearPage} from "../../../utils/render";
import {getToken} from "../../../utils/auths";


const tableEnTete = `
   <div style="justify-content: center; display: flex">
      <h1>Réception d'objets</h1>
   </div>
  <div style=" justify-content: center; display: flex">
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
      Authorization : getToken()
    },
  };
  const response = await fetch(`${process.env.API_BASE_URL}/objet`,opt);

  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  const datas = await response.json();
  console.log(datas)

  const tableBody = document.querySelector('.tableData');
  datas.forEach((objet)=>{
    tableBody.innerHTML += `
    <tr>
      <td class="receptionObjetsTd">${objet.idObjet}</td> 
      <td class="receptionObjetsTd">${objet.typeObjet.libelle}</td>
      <td class="td"><img src=/api/objet/getPicture/${objet.idObjet} alt="photo" width="100px"></td> 
      <td class="receptionObjetsTd">${objet.description}</td>
            <td class="receptionObjetsTd">${objet.etat ? changeEtatName(objet.etat) : '/'}</td>
      <td class="receptionObjetsTd">${objet.date_acceptation ? ` ${objet.date_acceptation[2]}/${objet.date_acceptation[1]}/${objet.date_acceptation[0]}` : '/' }</td>
          <td class="receptionObjetsTd">${objet.date_depot ? ` ${objet.date_depot[2]}/${objet.date_depot[1]}/${objet.date_depot[0]}` : '/' }</td>
      <td class="receptionObjetsTd"> ${objet.localisation ? objet.localisation  : '/' }</td>
      <td class="receptionObjetsTd">${objet.prix ? objet.prix: '/' }</td>
      <td class="receptionObjetsTd">${objet.date_vente ? ` ${objet.date_vente[2]}/${objet.date_vente[1]}/${objet.date_vente[0]}` : '/' }</td>
 
    </tr>`;
  })


}
function changeEtatName(etat){
  if (etat === 'accepte') {
    return  'Accepté'
  } if (etat === 'refuser') {
    return 'Refusé'
  }  if (etat === 'en vente') {
    return  'En Vente'
  }  if (etat === 'vendu') {
    return 'Vendu'
  }
  return  'Proposé'


}


const TableauDeBord =async () =>  {
  clearPage();
  const main = document.querySelector('main');
  const filterRechercheObjet =await filtrageObjet();
  main.innerHTML += filterRechercheObjet;
  head();
table();
  };

export default TableauDeBord;