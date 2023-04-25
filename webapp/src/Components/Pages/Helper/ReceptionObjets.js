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


  let dataHtml = ' ';
  async function getData() {
    try {
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

      const data = datas.filter((d) =>( d.etat === 'accepte'&& d.localisation === undefined) ||( d.etat === 'accepte' && d.localisation === "Atelier")  )

      const size = data.length;

      for (let i = 0; i < size;) {
        const currentLocation = data[i].localisation;
        dataHtml += `
    <tr>
      <td class="receptionObjetsTd">${data[i].idObjet}</td> 
      <td class="receptionObjetsTd">${data[i].typeObjet.libelle}</td>
      <td class="td"><img src=/api/objet/getPicture/${data[i].idObjet} alt="photo" width="100px"></td> 
      <td class="receptionObjetsTd">${data[i].description}</td>
      <td class="receptionObjetsTd">${data[i].date_acceptation[2]}/${data[i].date_acceptation[1]}/${data[i].date_acceptation[0]}</td>
      <td class="receptionObjetsTd"> 
        <select data-id =${data[i].idObjet}>
          <option value="" disabled ${currentLocation ? '' : 'selected'}>Localisation</option>
          <option  value="Magasin" ${currentLocation === 'Magasin' ? 'selected' : ''} ${currentLocation === 'Magasin' ? 'disabled' : ''}>Magasin</option>
          <option value="Atelier" ${currentLocation === 'Atelier' ? 'selected' : ''} ${currentLocation === 'Atelier' ? 'disabled' : ''}>Atelier</option>
        </select>
      </td>
      <td class="receptionObjetsTd">${data[i].date_depot ? data[i].date_depot  : '/' }</td>
      <td class="receptionObjetsTd">${data[i].prix_vente ? data[i].prix_vente : '/' }</td>
      <td class="receptionObjetsTd"><input type="button" value="Confirmer"></td>
      <td class="receptionObjetsTd"><button id="btnUpdate" class="btn btn-secondary" type="button">Modifier l'object</button></td>
    </tr>`;
        i += 1;
      }
      const tableBody = document.querySelector('.tableData');
      tableBody.innerHTML = dataHtml;

      const btnUpdate = document.querySelectorAll("#btnUpdate");
      btnUpdate.forEach((button) => {
        button.addEventListener("click", () => {
          const id = parseInt(button.parentNode.parentNode.querySelector("select").getAttribute("data-id"), 10);

          const objectSelectionner = data.find(element => element.idObjet === id);

          sessionStorage.setItem("objet", JSON.stringify(objectSelectionner));
          window.location.href = "/updatePage";
        });
      });

// Sélectionnez tous les select dans le corps de la table
      const selects = document.querySelectorAll('.tableData select');
// Ajoutez un événement change à chaque select
      selects.forEach(select => {
        select.addEventListener('change', async (event) => {
          event.preventDefault();
          const idObjet = event.target.dataset.id;
          const newLocation = event.target.value;
          try{
            const updatedLocalisation = {
              localisation:newLocation
            }
            const options = {
              method: 'POST',
              body: JSON.stringify(updatedLocalisation),
              headers: {
                'Content-Type': 'application/json',
                Authorization : getToken()
              },
            };


            const rep = await fetch(`${process.env.API_BASE_URL}/objet/depositObject/${idObjet}`, options);

            if (!rep.ok) {
                Swal.fire((await rep.text()).valueOf())
            }
          else{
              window.location.reload();
            }
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