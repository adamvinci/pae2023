import Swal from "sweetalert2";


// Récupérer l'objet de la session
let object = sessionStorage.getItem("objet");
if (!object) {
  // L'objet n'existe pas encore, on peut définir une valeur par défaut
  object = {
    idObjet: '',
    typeObjet: {libelle: ''},
    description: '',
    etat: null,
    date_acceptation: null,
    date_depot: null,
    localisation: null,
    prix: null,
    utilisateur: ''
  };
} else {
  object = JSON.parse(object);
}
const selectedObject = `

 <div class="titre1">
  <h1>Objet selectionner</h1>
 </div>

  <div id="tableContainer">
    <table id="tableUpdate" class="table table-hover">
      <thead> 
        <tr> 
          <th> Id Objet </th> 
          <th> Type d'objet </th>
          <th> Photo objet </th>
          <th> Description </th>
          <th> Etat </th>
          <th> Date d'acceptation </th>
          <th> Date de depot </th>
          <th> Localisation </th>
          <th> Prix </th>
          <th> Utilisateur </th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>${object.idObjet}</td> 
          <td>${object.typeObjet.libelle}</td>
          <td><img src=/api/objet/getPicture/${object.idObjet} alt="photo" width="100px"></td> 
          <td>${object.description}</td>
          <td>${object.etat ? `${object.etat}` : '/'}</td>
          <td>${object.date_acceptation
    ? `${object.date_acceptation[2]}/${object.date_acceptation[1]}/${object.date_acceptation[0]}`
    : '/'}</td>
          <td>${object.date_depot
    ? `${object.date_depot[2]}/${object.date_depot[1]}/${object.date_depot[0]}`
    : '/'}</td>
          <td>${object.localisation ? `${object.localisation}` : '/'}</td>
          <td>${object.prix ? `${object.prix}€` : '/'}</td>
          <td>${object.utilisateur}</td>
        </tr>
      </tbody>    
    </table>
  </div>`;

async function getTypeObject() {

  const options = {
    method: 'GET',
  };
  const response = await fetch(`${process.env.API_BASE_URL}/objet/typeObjet`,
      options);

  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  const objets = await response.json();

  return objets;
}

async function filtrageObjet() {
  let optionsHtml;
  const typeObjet = await getTypeObject();
  optionsHtml = '';
  for (let i = 0; i < typeObjet.length;) {
    const type = typeObjet[i];

    optionsHtml += `<option value="${type.idObjet}">${type.libelle}</option>`;
    i += 1;
  }
  const updateObject = `

    <div id="formulaireContainer">
      <div class="titre">
        <br>
        <h2> Mettre à jour l'objet  </h2>
        <br>
      </div>
      <br>
      <div id="formulaireUpdate">
        <form>
          <select class="form-select" id = "type" aria-label="Default select example">
           <option disabled selected >Type d'objet</option>
              ${optionsHtml}
          </select>
          <textarea id="description" class="form-control"  placeholder="Modifier la description maximum 120 caractères"></textarea>
          <input class="form-control" id="photo" name="file" type= "file" />
          <input id="updateBtn" type="button" value="Mettre a jour">
       </form>
       
     </div>
  `;
  return updateObject;
}

async function majObject(stringPicture) {
  const objectId = JSON.parse(sessionStorage.getItem("objet"));
  const id = objectId.idObjet;

  const description = document.getElementById("description").value;
  const type = document.getElementById("type").value;

  const photo = stringPicture.name;

  try {
    const options = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        description,
        type,
        photo,
      }),
    };

    const response = await fetch(`${process.env.API_BASE_URL}/objet/updateObject/${id}`, options);

    if (!response.ok) {
      Swal.fire((await response.text()).valueOf())
    }else {
      Swal.fire({
        title: 'Mise à jour réussie !',
        text: 'Votre mise à jour a été effectuée avec succès.',
        timer: 5000
      }).then(() => {
        window.location.href = '/TableauDeBord';
      });
    }
  } catch (error) {
    console.error(error);
  }
}


async function sendPicture(e){
  e.preventDefault();
  e.stopPropagation();
  let stringPicture = '';
  if (document.querySelector("input[name=file]").files[0] === undefined) {
    majObject(stringPicture);

  } else {
    try {

      if (document.querySelector("input[name=file]").files.length > 0) {
        [stringPicture] = document.querySelector("input[name=file]").files;
      }

      const fileInput = document.getElementById("photo");

      const formData = new FormData();

      formData.append("file", fileInput.files[0]);
      const options = {
        method: "POST",
        body: formData,
      };

      const response = await fetch(`${process.env.API_BASE_URL}/objet/upload`, options);

      if (!response.ok) {
        Swal.fire((await response.text()).valueOf())
      }else{
        majObject(stringPicture);
      }

    } catch (error) {
      console.error(error);
    }
  }
}

async function head() {
  const main = document.querySelector('main');
  main.innerHTML += selectedObject;
  main.innerHTML += await filtrageObjet();

  const updateBtn = document.getElementById("updateBtn");
  updateBtn.addEventListener("click", sendPicture);
}

const UpdateObjetPage = async () => {
  await head();
};

export default UpdateObjetPage;
