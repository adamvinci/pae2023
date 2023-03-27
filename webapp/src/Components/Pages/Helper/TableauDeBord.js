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
            <td class="receptionObjetsTd">${objet.etat ? objet.etat : '/'}</td>
      <td class="receptionObjetsTd">${objet.date_acceptation ? ` ${objet.date_acceptation[2]}/${objet.date_acceptation[1]}/${objet.date_acceptation[0]}` : '/' }</td>
          <td class="receptionObjetsTd">${objet.date_depot ? ` ${objet.date_depot[2]}/${objet.date_depot[1]}/${objet.date_depot[0]}` : '/' }</td>
      <td class="receptionObjetsTd"> ${objet.localisation ? objet.localisation  : '/' }</td>
      <td class="receptionObjetsTd">${objet.prix ? objet.prix: '/' }</td>
      <td class="receptionObjetsTd">${objet.date_vente ? ` ${objet.date_vente[2]}/${objet.date_vente[1]}/${objet.date_vente[0]}` : '/' }</td>
 
    </tr>`;
})


}
const TableauDeBord =async () => {
  clearPage();
   head();
   table();
  };
  
  export default TableauDeBord;