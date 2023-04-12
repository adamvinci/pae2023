import Swal from "sweetalert2";
import { getAuthenticatedUser, getToken } from "../../utils/auths";

let userID;

const UserPage = async () => {
  const loggedUser = await getAuthenticatedUser();
  userID = loggedUser.id;
  const main = document.querySelector('main');

  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization : getToken()
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/auths/getPicture/${userID}`, options);
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
                                    <input type="file" name="myImage" accept=".png, .jpg, .jpeg"/>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Nom</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="userName" type="text" class="form-control" value="${loggedUser.nom}">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Prénom</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="userFirstName" type="text" class="form-control" value="${loggedUser.prenom}">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">Email</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="userEmail" type="text" class="form-control" value="${loggedUser.email}">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-sm-3">
                                <h6 class="mb-0">GSM</h6>
                            </div>
                            <div class="col-sm-9 text-secondary">
                                <input id="userGsm" type="text" class="form-control" value="${loggedUser.gsm}">
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
                                <input type="text" class="form-control" value="${loggedUser.dateInscription}" disabled="disabled">
                            </div>
                        </div>
                    
                        <div class="row">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-9 text-secondary">
                                <input id="saveButton" type="button" class="btn btn-primary px-4" value="Sauvegarder les changements">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>`;

const saveButton = document.querySelector('#saveButton');
saveButton.addEventListener('click', updateUser)

};


async function updateUser(e) {  
    e.preventDefault();
    const nom = document.querySelector('#userName').value;
    const prenom = document.querySelector('#userFirstName').value;
    const email = document.querySelector('#userEmail').value;
    const gsm = document.querySelector('#userGsm').value;
    const fileInput = document.querySelector('input[name=myImage]');
    let img;

    if(fileInput.files[0] !== undefined){
        img = fileInput.files[0].name;
        const formData = new FormData();
        formData.append('file', fileInput.files[0]);
        const options1 = {
            method: 'POST',
            body: formData
        };
        fetch(`${process.env.API_BASE_URL}/auths/upload`, options1);
    }

    const options = {
        method: 'PUT',
        body: JSON.stringify({
            nom,
            prenom,
            email,
            gsm,
            image: img
        }),
        headers: {
          'Content-Type': 'application/json',
          Authorization : getToken()
        },
      };
    
      const response = await fetch(`${process.env.API_BASE_URL}/users/${userID}`, options);
      if (!response.ok) {
        Swal.fire((await response.text()).valueOf())
      }
    
      Swal.fire({
        position: 'top-end',
        icon: 'success',
        title: "Votre profil a été modifié avec succès",
        showConfirmButton: false,
        timer: 1500
      })
      UserPage(); // eslint-disable-line no-use-before-define

    
}
export default UserPage;
