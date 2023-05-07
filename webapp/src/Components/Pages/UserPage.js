import Swal from "sweetalert2";
import { getAuthenticatedUser, getToken } from "../../utils/auths";
import Navigate from "../Router/Navigate";
import navbar from "../Navbar/Navbar";


let userID;

const UserPage = async () => {
  const loggedUser = await getAuthenticatedUser();
  if(loggedUser === undefined) {
    Navigate('/');
    return;
  }
  userID = loggedUser.id;
  const main = document.querySelector('main');

  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization : getToken()
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/users/getPicture/${userID}`, options);
  const img = await response.blob();
  main.innerHTML = `<div class="container">
    <div class="main-body">
        <div class="row">
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-body">
                        <div class="d-flex flex-column align-items-center text-center">
                            <img src=${URL.createObjectURL(img)} alt="Admin" class="rounded-circle p-1 bg-primary" width="110">
                            <div class="mt-3">
                                <h4>${loggedUser.prenom} ${loggedUser.nom}</h4>
                                <form>
                                    <label>Modifier la photo</label>
                                    <input type="file" name="myImage" accept="image/*""/>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-body">
                    <form id="userForm">
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Nom</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="userName" type="text" class="form-control" value="${loggedUser.nom}" required>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Prénom</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="userFirstName" type="text" class="form-control" value="${loggedUser.prenom}" required>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Email</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="userEmail" type="text" class="form-control" value="${loggedUser.email}" required>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">GSM</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="userGsm" type="text" class="form-control" value="${loggedUser.gsm}" required>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Nouveau mot de passe </h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="userPassword" type="password" class="form-control">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Confimer nouveau mot de passe</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="confirmPassword" type="password" class="form-control">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Rôle</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" value="${loggedUser.role}" disabled="disabled">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Date d'inscription </h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input type="text" class="form-control" value="${loggedUser.dateInscription[2]}/${loggedUser.dateInscription[1]}/${loggedUser.dateInscription[0]}" disabled="disabled">
                            </div>
                        </div>
                    
                        <div class="row">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-9 text-secondary">
                                <button id="saveButton" type="submit" class="btn btn-primary px-4">Sauvegarder les changements</button>
                            </div>
                        </div>
                    </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>`;

const userForm = document.querySelector('#userForm');
userForm.addEventListener('submit', checkPassword)

};

function checkPassword(e){
    e.preventDefault();
    Swal.fire({
        title: '<strong>Entrez votre mot de passe actuel pour confirmer les changements </strong>',
        icon: 'info',
        html:
          '<input id="password" type="password" class="form-control">',
        showCloseButton: true,
        showCancelButton: true,
        focusConfirm: false,
        confirmButtonText:
          '<i class="fa fa-thumbs-up"></i> Confirmer',
        cancelButtonText:
          '<i class="fa fa-thumbs-down"> Annuler </i>',
        }).then((result) => {
            if (result.isConfirmed) {
              updateUser();
            } 
      })
}


async function updateUser() {  
    const nom = document.querySelector('#userName').value;
    const prenom = document.querySelector('#userFirstName').value;
    const email = document.querySelector('#userEmail').value;
    const gsm = document.querySelector('#userGsm').value;
    const password = document.querySelector('#userPassword').value;
    const confirmPassword = document.querySelector('#confirmPassword').value;
    const fileInput = document.querySelector('input[name=myImage]');
    const actualPassword = document.querySelector('#password').value;


    let img;

    if(fileInput.files[0] !== undefined){
        img = fileInput.files[0].name;
        const formData = new FormData();
        formData.append('file', fileInput.files[0]);
        const options1 = {
            method: 'POST',
            body: formData
        };
      const responseImage = await fetch(`${process.env.API_BASE_URL}/users/upload`, options1);

      if (!responseImage.ok) {
        Swal.fire((await responseImage.text()).valueOf())
      }
      const imageSaved =  await responseImage.text();
      img = imageSaved

    }

    const options = {
        method: 'PUT',
        body: JSON.stringify({
            nom,
            prenom,
            email,
            gsm,
            image: img,
            password,
            confirmPassword,
            actualPassword
        }),
        headers: {
          'Content-Type': 'application/json',
          Authorization : getToken()
        },
      };
    
      const response = await fetch(`${process.env.API_BASE_URL}/users/${userID}`, options);
      if (!response.ok) {
        Swal.fire((await response.text()).valueOf())
      }else{
    
      Swal.fire({
        position: 'top-end',
        icon: 'success',
        title: "Votre profil a été modifié avec succès",
        showConfirmButton: false,
        timer: 1500
      })
        navbar();
    }
    UserPage(); // eslint-disable-line no-use-before-define
  
}
export default UserPage;
