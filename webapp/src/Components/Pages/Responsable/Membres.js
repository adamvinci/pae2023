const utilisateursTests = [
  {nom : 'Agbassah', prenom : 'Steven', email : 'steven.agbassah@student.vinci.be'},
  {nom : 'Jacques', prenom : 'Jean', email : 'jean.jacques@student.vinci.be'},
  {nom : 'Agbassah', prenom : 'Roy', email : 'Roy.agbassah@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  {nom : 'Bertrand', prenom : 'Amar', email : 'bertrand.amar@student.vinci.be'},
  
];
console.log(utilisateursTests);

const renderUsersTable = async () => {
  const main = document.querySelector('main');
  const UsersTable = document.createElement('table');
  UsersTable.className = 'table';
  UsersTable.innerHTML =  `<thead class="thead-light">
                              <tr>
                              <th scope="col">#</th>
                              <th scope="col">Prénom</th>
                              <th scope="col">Nom</th>
                              <th scope="col">Mail</th>
                              <th scope="col">Aidant</th>
                              </tr>
                          </thead>
                          <tbody>`  
  
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/users`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);

  const users = await response.json();

  for (let index = 0; index < users.length; index+=1) {
      UsersTable.innerHTML += `<tr>
      <th scope="row">${index + 1}</th>
      <td>${users[index].prenom}</td>
      <td>${users[index].nom}</td>
      <td>${users[index].email}</td>
      <td><button type="button" class="btn btn-success">Indiquer aidant</button></td>
  </tr>`

  }

  UsersTable.innerHTML += `</tbody>
                          </table>`
  main.appendChild(UsersTable);
}

const Membres = () => {
  const main = document.querySelector('main');
  main.innerHTML = `<h3>Listes utilisateurs`;
  renderUsersTable();
};


export default Membres;

