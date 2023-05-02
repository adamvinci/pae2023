import Swal from "sweetalert2";
import {getAuthenticatedUser, getToken } from "../../../utils/auths";
import { clearPage } from "../../../utils/render";
import Navigate from "../../Router/Navigate";


const confirmHelper = async (e) => {
  e.preventDefault();
  const userID = e.target.parentNode.children[0].value;

  const options = {
    method: 'POST',
    body: JSON.stringify({
    }),
    headers: {
      'Content-Type': 'application/json',
      Authorization : getToken()
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/users/${userID}/confirmHelper`, options);
  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  Swal.fire({
    position: 'top-end',
    icon: 'success',
    title: "L'utilisateur est désormais un 'aidant' ! ",
    showConfirmButton: false,
    timer: 1500
  })
  Membres(); // eslint-disable-line no-use-before-define

}

const confirmResponsable = async (e) => {
  e.preventDefault();
  const userID = e.target.parentNode.children[0].value;

  const options = {
    method: 'POST',
    body: JSON.stringify({
    }),
    headers: {
      'Content-Type': 'application/json',
      Authorization : getToken()
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/users/${userID}/confirmManager`, options);
  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  Swal.fire({
    position: 'top-end',
    icon: 'success',
    title: "L'utilisateur est désormais un 'responsable' ! ",
    showConfirmButton: false,
    timer: 1500
  })
  Membres(); // eslint-disable-line no-use-before-define

}


const renderUsersTable = async () => {
  const main = document.querySelector('main');
  const usersTable = document.createElement('table');
  const tableWrapper = document.createElement('div');
  tableWrapper.className = 'table-responsive';
  usersTable.className = 'table table-hover';
  usersTable.innerHTML =  `<thead class="thead-light">
                              <tr>
                              <th scope="col">#</th>
                              <th scope="col">Prénom</th>
                              <th scope="col">Nom</th>
                              <th scope="col">Mail</th>
                              <th scope="col">GSM</th>
                              <th scope="col">Date d'inscription</th>
                              <th scope="col">Rôle</th>
                              <th scope="col">Confirmer aidant</th>
                              <th scope="col">Confirmer responsable</th>
                              </tr>
                          </thead>
                          <tbody>`  
  
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization : getToken()
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/users`, options);

  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }


  const users = await response.json();

  users.sort((a, b) => {
    const fa = a.nom.toLowerCase();
    const fb = b.nom.toLowerCase();

    if (fa < fb) {
        return -1;
    }
    if (fa > fb) {
        return 1;
    }
    return 0;
});

  for (let index = 0; index < users.length; index+=1) {
      let usersTableHTML = `<tr>
      <th scope="row">${index + 1}</th>
      <td>${users[index].prenom}</td>
      <td>${users[index].nom}</td>
      <td>${users[index].email}</td>
      <td>${users[index].gsm}</td>
      <td>${users[index].dateInscription[2]}/${users[index].dateInscription[1]}/${users[index].dateInscription[0]}</td>
      <td>${users[index].role}</td>
      
      `
      if(users[index].role === 'membre' ) {usersTableHTML +=  `
      <td>
      <form class = "confirmHelperButton"> 
      <input type="hidden" id="userId" name="userId" value="${users[index].id}" />
      <button type="submit" class="btn btn-success">Confirmer aidant</button>
      </form>
      </td>`
  }
      else {usersTableHTML +=  `<td> Action Impossible </td>`}
      if(users[index].role === 'membre' || users[index].role ==='aidant' ) {
        usersTableHTML +=  `
      <td>
      <form class = "confirmResponsableButton"> 
      <input type="hidden" id="userId" name="userId" value="${users[index].id}" />
      <button type="submit" class="btn btn-success">Confirmer responsable</button>
      </form>
      </td>
      `
      }
      else {usersTableHTML +=  `<td> Action Impossible </td>`}
      usersTableHTML += `</tr>`

      usersTable.innerHTML += usersTableHTML;
  }

  usersTable.innerHTML += `</tbody>
                          </table>`
  
  tableWrapper.appendChild(usersTable);
  main.appendChild(tableWrapper);
  const forms = document.getElementsByClassName('confirmHelperButton');
  for (let i = 0; i < forms.length; i+=1) {
    forms[i].addEventListener("click", confirmHelper)
    
  }

  const confirmResponsableButtons = document.getElementsByClassName('confirmResponsableButton');
  for (let i = 0; i < forms.length; i+=1) {
    confirmResponsableButtons[i].addEventListener("click", confirmResponsable);
    
  }
}

const Membres = async () => {
  clearPage();
  const user = await getAuthenticatedUser();
  if(user === undefined ||  user.role !== "responsable" ) {
    Navigate('/');
    return;
  }
  const main = document.querySelector('main');
  main.innerHTML = `<h3>Listes utilisateurs</h3>`;
  renderUsersTable();
};




export default Membres;

