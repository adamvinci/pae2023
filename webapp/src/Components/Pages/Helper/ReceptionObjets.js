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

  const data=[];
  let dataHtml = ' ';
  async function getData() {
    try {
      const response = await fetch(`${process.env.API_BASE_URL}/objet`);

      if (!response.ok) {
        throw new Error('Network response was not ok.');
      }

      const datas = await response.json();
      let j=0;
      for (let i = 0; i < datas.length; i+=1) {
        if(datas[i].etat!=="proposer" && datas[i].etat!=="vendu"){
          data[j]=datas[i];
          j+=1;
        }
      }
      const size = data.length;

      for (let i = 0; i < size;) {
        const currentLocation = data[i].localisation;
        dataHtml += `
    <tr style="font-family: 'Games', sans-serif;" data-idobjet="${data[i].idObjet}">
      <td class="receptionObjetsTd">${data[i].utilisateur}</td> 
      <td class="receptionObjetsTd">${data[i].typeObjet.libelle}</td>
      <td class="td"><img src=/api/objet/getPicture/${data[i].idObjet} alt="photo" width="100px"></td> 
      <td class="receptionObjetsTd">${data[i].description}</td>
      <td class="receptionObjetsTd">${data[i].date_acceptation}</td>
      <td class="receptionObjetsTd"> 
        <select>
          <option value="" disabled ${currentLocation ? '' : 'selected'}>Localisation</option>
          <option value="magasin" ${currentLocation === 'magasin' ? 'selected' : ''} ${currentLocation === 'magasin' ? 'disabled' : ''}>Magasin</option>
          <option value="atelier" ${currentLocation === 'atelier' ? 'selected' : ''} ${currentLocation === 'atelier' ? 'disabled' : ''}>Atelier</option>
        </select>
      </td>
      <td class="receptionObjetsTd">${data[i].date_depot}</td>
      <td class="receptionObjetsTd">${data[i].prix_vente}</td>
      <td class="receptionObjetsTd"><input type="button" value="Confirmer"></td>
      <td class="receptionObjetsTd"><input type="button" value="Modifier"></td>
    </tr>`;
        i += 1;
      }
      const tableBody = document.querySelector('.tableData');
      tableBody.innerHTML = dataHtml;

// Sélectionnez tous les select dans le corps de la table
      const selects = document.querySelectorAll('.tableData select');
// Ajoutez un événement change à chaque select
      selects.forEach(select => {
        select.addEventListener('change', async (event) => {
          event.preventDefault();
          const idObjet = event.target.closest('tr').dataset.idobjet;
          const newLocation = event.target.value;
          try{
            const options = {
              method: 'POST',
              body: JSON.stringify({
                newLocation,
              }),
              headers: {
                'Content-Type': 'application/json',
                Authorization : getToken()
              },
            };


            const rep = await fetch(`${process.env.API_BASE_URL}/objet/depositObject/${idObjet}`, options);

            if (!rep.ok) throw new Error(`fetch error : ${rep.status} : ${rep.statusText}`);
            window.location.reload();
          }
          catch (err){
            throw Error(err);
          }
        });
      });

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