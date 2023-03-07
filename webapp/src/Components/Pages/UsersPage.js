const utilisateursTests = [
    {nom : 'Agbassah', prenom : 'Steven', mail : 'steven.agbassah@student.vinci.be'},
    {nom : 'Jacques', prenom : 'Jean', mail : 'jean.jacques@student.vinci.be'},
    {nom : 'Agbassah', prenom : 'Roy', mail : 'Roy.agbassah@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    {nom : 'Bertrand', prenom : 'Amar', mail : 'bertrand.amar@student.vinci.be'},
    
];
console.log(utilisateursTests);

const renderUsersTable = () => {
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
    

    for (let index = 0; index < utilisateursTests.length; index+=1) {
        UsersTable.innerHTML += `<tr>
        <th scope="row">${index + 1}</th>
        <td>${utilisateursTests[index].prenom}</td>
        <td>${utilisateursTests[index].nom}</td>
        <td>${utilisateursTests[index].mail}</td>
        <td><button type="button" class="btn btn-success">Indiquer aidant</button></td>
    </tr>`

    }

    UsersTable.innerHTML += `</tbody>
                            </table>`
    main.appendChild(UsersTable);
}

const UsersPage = () => {
    const main = document.querySelector('main');
    main.innerHTML = `<h3>Listes utilisateurs`;
    renderUsersTable();
  };


  export default UsersPage;
  
  