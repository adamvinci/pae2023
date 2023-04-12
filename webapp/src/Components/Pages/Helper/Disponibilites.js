import Swal from "sweetalert2";
import {getAuthenticatedUser, getToken} from "../../../utils/auths";


const Disponibilites = async () => {
  const user = await getAuthenticatedUser();
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization : getToken()
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/auths/getPicture/${user.id}`, options);

  const img1 = await response.blob();
  const main = document.querySelector('main');
  main.innerHTML =`
  <form >
<div id = disponibilitePage>

  <div id =disponibiliteForm>
  <div id = disponibiliteTitle>
  <h1>Disponibilités</h1>
</div>

<div id=disponibiliteTable>

<table id = disponibiliteTablle>

  <tr>
    <th>Disponibilite/th>
    <th>Plage Horaire</th>
    <th>Photo</th>
    <th>Nom</th>
    <th>Prenom</th>
    <th>Email</th>
    <th>Gsm</th>
  </tr>
  <tr>
<td>
 <input type="date" id="disponibilite" min="2023-04-08" step="7"  required > 
 </td>
 <td>  
 <select name="plage" required>
      <option value="" disabled selected hidden>Choisisez une plage horaire</option>
      <option value="matin">Matin</option>
      <option value="apres midi">Apres-Midi</option>

    </select>
    </td>
    <td><img src=${URL.createObjectURL(img1)} height="100px"  ></td>
    <td>${user.nom}</td>
    <td>${user.prenom}</td>
    <td>${user.email}</td>
    <td>${user.gsm ? user.gsm : '/'}</td>
    
  </tr>

</table>
</div>
<div id = disponibiliteFormBtnG>
<div id = disponibiliteFormBtn>

<input type="submit" id ="btnForm" value="Ajouter une Disponibilité" />
</div>
</div>
  </div>
  </div>
</form>
  `
  const inputDate = document.getElementById("disponibilite");

  inputDate.addEventListener("input", (event) => {
    const selectedDate = new Date(event.target.value);
    const dayOfWeek = selectedDate.getDay();
    if (dayOfWeek !== 6) {
      event.target.setCustomValidity("Veuillez choisir un samedi.");
    } else {
      event.target.setCustomValidity("");
    }
  });
  const formD=document.querySelector("form")
  formD.addEventListener('submit', async (e)=>{
    e.preventDefault();
    const date = document.getElementById("disponibilite").value;
    const plage = document.getElementsByName("plage")[0].value;
    const options1 = {
      method: 'POST',
      body:JSON.stringify({
          date,
          plage
    }),
      headers: {
        'Content-Type': 'application/json',
        Authorization : getToken()
      },
    };

    const response1 = await fetch(`${process.env.API_BASE_URL}/disponibilite`, options1);
    if (!response1.ok) {
      Swal.fire((await response1.text()).valueOf())
    }
    Swal.fire({
      position: 'top-end',
      icon: 'success',
      title: "La disponibilite a bien ete ajouté ",
      showConfirmButton: false,
      timer: 1500
    })
    Disponibilites();
  });
};



  export default Disponibilites;