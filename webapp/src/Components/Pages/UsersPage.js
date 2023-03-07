const renderUsersList = () => {
    const main = document.querySelector('main');
    const UsersListHTML = document.createElement('ul');
    const element1 = document.createElement('li');
    element1.innerHTML = "Utilisateur 1";
    const element2 = document.createElement('li');
    element2.innerHTML = "Utilisateur 0";
    UsersListHTML.appendChild(element1);
    UsersListHTML.appendChild(element2);
    main.appendChild(UsersListHTML);
}

const UsersPage = () => {
    const main = document.querySelector('main');
    main.innerHTML = `<h3>Listes utilisateurs`;
    renderUsersList();
  };


  export default UsersPage;
  
  