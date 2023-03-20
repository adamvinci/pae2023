import {getAuthenticatedUser, getToken } from "../../../utils/auths";
import { clearPage } from "../../../utils/render";
import Navigate from "../../Router/Navigate";

// const utilisateursTests = [
//   {nom : 'Agbassah', prenom : 'Steven', email : 'steven.agbassah@student.vinci.be'},
//   {nom : 'Jacques', prenom : 'Jean', email : 'jean.jacques@student.vinci.be'},
//   {nom : 'Agbassah', prenom : 'Roy', email : 'Roy.agbassah@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
//   {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  
// ];
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
                              <th scope="col">Aidant</th>
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

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);

  const users = await response.json();

  for (let index = 0; index < users.length; index+=1) {
      let usersTableHTML = `<tr>
      <th scope="row">${index + 1}</th>
      <td>${users[index].prenom}</td>
      <td>${users[index].nom}</td>
      <td>${users[index].email}</td>
      <td>${users[index].gsm}</td>
      <td>${users[index].dateInscription}</td>
      <td>${users[index].role}</td>
      `
      if(users[index].role === 'membre' ) {usersTableHTML +=  `<td><button type="button" class="btn btn-success">Indiquer aidant</button></td>`}
      else {usersTableHTML +=  `<td> Action impossible !</td>`}
      usersTableHTML += `</tr>`

      usersTable.innerHTML += usersTableHTML;
  }

  usersTable.innerHTML += `</tbody>
                          </table>`
  
  tableWrapper.appendChild(usersTable);
  main.appendChild(tableWrapper);
}

const Membres = async () => {
  clearPage();
  const user = await getAuthenticatedUser();
  if(user === undefined ||  user.role !== "responsable" ) {
    Navigate('/');
  }
  const main = document.querySelector('main');
  main.innerHTML = `<h3>Listes utilisateurs</h3>`;
  renderUsersTable();
};


export default Membres;

