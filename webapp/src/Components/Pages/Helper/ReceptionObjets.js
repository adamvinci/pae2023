import {clearPage} from "../../../utils/render";

const tableEnTete = `
   <div style="justify-content: center; display: flex">
      <h1>Réception d'objets</h1>
   </div>
  <div style=" justify-content: center; display: flex">
    <table class="tableEnTete">
      <thead> 
        <tr> 
          <th class="receptionObjetsTh"> Id utilisateur </th> 
          <th class="receptionObjetsTh"> Type d'objet </th>
          <th class="receptionObjetsTh"> Photo objet </th>
          <th class="receptionObjetsTh"> Description </th>
          <th class="receptionObjetsTh"> Date de reception </th>
          <th class="receptionObjetsTh"> Localisation </th>
          <th class="receptionObjetsTh"> Date dépot </th>
          <th class="receptionObjetsTh"> Prix </th>
          <th class="receptionObjetsTh"> Confirmation </th>
          <th class="receptionObjetsTh"> Modifier </th>
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

function table() {

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
      console.log(data);
      const size = data.length;

      for (let i = 0; i < size;) {
        dataHtml += `
        <tr style="font-family: 'Games', sans-serif;">
          <td class="receptionObjetsTd">${data[i].utilisateur}</td> 
          <td class="receptionObjetsTd">${data[i].typeObjet.libelle}</td>
          <td class="td"><img src=/api/objet/getPicture/${data[i].idObjet} alt="photo" width="100px"></td> 
          <td class="receptionObjetsTd">${data[i].description}</td>
          <td class="receptionObjetsTd">${data[i].date_acceptation}</td>
          <td class="receptionObjetsTd"> 
            <select>
              <option value="" disabled selected>Localisation</option>
              <option value="magasin">Magasin</option>
              <option value="atelier">Atelier</option>
            </select>
          </td>
          <td class="receptionObjetsTd">${data[i].date_depot}</td>
          <td class="receptionObjetsTd">${data[i].prix_vente}</td>
          <td class="receptionObjetsTd"><input type="button" value="Confirmer"></td>
          <td class="receptionObjetsTd"><input type="button" value="Modifier"></td>
         </tr>`;
        i += 1
      }
      const tableBody = document.querySelector('.tableData');
      tableBody.innerHTML = dataHtml;

    } catch (error) {
      throw new Error(error);
    }
  }
  getData();
}


const ReceptionObjets = () => {
  clearPage();
  head();
  table();
};

export default ReceptionObjets;