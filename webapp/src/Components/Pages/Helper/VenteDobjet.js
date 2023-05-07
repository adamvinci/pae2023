import Swal from "sweetalert2";
import {clearPage} from "../../../utils/render";
import {getAuthenticatedUser, getToken} from "../../../utils/auths";

const tableEnTete = `
  <div style=" justify-content: center; display: flex"><h1>Gestion de la vente</h1></div>
  <div style=" justify-content: center; display: flex">
  
    <table class="tableEnTete">
      <thead> 
        <tr> 
                  <th class="rechercheObjetsTh"> Photo objet </th>
          <th class="rechercheObjetsTh"> Type d'objet </th> 
          <th class="rechercheObjetsTh"> Description </th> 
          <th class="rechercheObjetsTh"> date de dépot </th>
          <th class="rechercheObjetsTh"> Prix de l'objet </th>
          <th class="rechercheObjetsTh"> Etat de vente </th>
 
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



function homeScreen(){
  let data;
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
      data = datas.filter((d)=>d.etat === 'en vente' || (d.etat === 'accepte' && d.localisation ==='Magasin'));
      const size = data.length;

      const tableBody = document.querySelector('.tableData');
      for (let i = 0; i < size;) {
        dataHtml = `
        <tr>
                  <td class="td"><img src=/api/objet/getPicture/${data[i].idObjet} alt="photo" width="100px"></td> 
          <td class="rechercheObjetsTd">${data[i].typeObjet.libelle}</td> 
          <td class="rechercheObjetsTd">${data[i].description}</td>
          <td class="rechercheObjetsTd">${data[i].date_depot[2]}/${data[i].date_depot[1]}/${data[i].date_depot[0]}</td>
          <td class="rechercheObjetsTd" id="prixDonne">`;

        if (data[i].prix) {
          dataHtml += `${data[i].prix}€`;
        } else {
          dataHtml += `<input type="text" size="1" style="background-color: beige" id="prix-${data[i].idObjet}">
                      <button class="confirmer" data-id="${data[i].idObjet}">Confirmer</button>`;
        }

        dataHtml += `</td>
          <td class="rechercheObjetsTd">`;
        if (data[i].etat === "en vente") {
          dataHtml += `<button  class="buttonVendu" size="1" data-index="${data[i].idObjet}" style="background-color: indianred">Indiquer vendu</button>`;
        }else{
          dataHtml += `<p>${changeEtatName(data[i].etat)}</p>`;
        }

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

            if (!rep.ok) Swal.fire((await rep.text()).valueOf())
            else{
              window.location.reload();
            }
          }
          catch (err){
            throw Error(err);
          }

        });
      });

      const auths=async ()=>{
        const ath=await getAuthenticatedUser()
        return ath;
      }


      const confirmerBtns = document.querySelectorAll('.confirmer');
      confirmerBtns.forEach(btn => {
        btn.addEventListener('click', async (event) => {
          const authenticatedUser =await auths();
          const idObjet = event.target.dataset.id;
          const prixInput = document.querySelector(`#prix-${idObjet}`);
          const prix = prixInput.value;
          const options = {
            method: 'POST',
            body: JSON.stringify({
              prix,
            }),
            headers: {
              'Content-Type': 'application/json',
              Authorization : getToken()
            }};
          // Function mis en vente
          const misEnVente=async()=>{

            try{

              const rep = await fetch(`${process.env.API_BASE_URL}/objet/misEnVenteObject/${idObjet}`, options);

              if (!rep.ok) Swal.fire((await rep.text()).valueOf())
              else{
                window.location.reload();
              }
            }
            catch (err){
              throw Error(err);
            }
          }
          // Function vendu
          const vendu = async()=>{

            try{

              const rep = await fetch(`${process.env.API_BASE_URL}/objet/vendreObjectResponsable/${idObjet}`, options);

              if (!rep.ok) Swal.fire((await rep.text()).valueOf())
              else{
                window.location.reload();
              }
            }
            catch (err){
              throw Error(err);
            }
          }
          if(authenticatedUser?.role==='responsable'){
            const swalWithBootstrapButtons = Swal.mixin({
              customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
              },
              buttonsStyling: false
            })

            swalWithBootstrapButtons.fire({
              title: 'Choisissez l\'état de l\'objet ?',
              showCancelButton: true,
              confirmButtonText: 'En vente',
              cancelButtonText: 'Vendu',
              reverseButtons: true
            }).then((result) => {
              if (result.isConfirmed) {
                misEnVente();

              } else if (
                  /* Read more about handling dismissals below */
                  result.dismiss === Swal.DismissReason.cancel
              ) {
                vendu();

              }
            })
          }else{
            misEnVente();
          }


        });
      });



    } catch (error) {
      throw new Error(error);
    }
  }
  getData();
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
const VenteDobjet =async () => {
  clearPage();
   head();
  homeScreen();
  };
  
  export default VenteDobjet;