import Swal from "sweetalert2";
import { clearPage } from '../../utils/render';
import {getAuthenticatedUser,getToken} from '../../utils/auths'
import Navigate from "../Router/Navigate";


const AddObjetPage = () => {
  clearPage();

  renderAddObjetForm();
};


async function renderAddObjetForm() {
  const main = document.querySelector('main');
  const newDiv = document.createElement("div");
  const form = document.createElement('form');
  form.className = 'p-5';
  const gsm = document.createElement('input');
  gsm.type = 'text';
  gsm.id = 'gsm';
  gsm.placeholder = 'Numero de telephone';
  gsm.required = "true";
  const label = document.createElement('label')
  label.innerText = "Choisissez une image"
  const inputForm = document.createElement('input')
  inputForm.type = "file"
  inputForm.name = "file"
  inputForm.accept ="image/*"
  inputForm.required = "true"

  const description = document.createElement('input');
  description.id="description"
  description.type="text"
  description.placeholder="Description";
  description.required = "true"

  const submit = document.createElement('input');
  submit.value = 'ajouter';
  submit.type = 'submit';
  submit.className = 'btn btn-info';

  const br = document.createElement("br");
  const dispo= await getDispo();

  const typeObjet = await getTypeObject();

  let typeObjString= `<label for="type-select">choisis un type d'objet:</label>
   <select required name="type" id="type-select">
    <option value="">--Please choose an option--</option>`


  typeObjet.forEach((typee) => {
   typeObjString+= `<option id="ok" value=${typee.idObjet}>${typee.libelle}</option>`
  })

  typeObjString+=`</select>  <br/>`

  typeObjString+= `<label for="dispo-select">choisis une disponibilité:</label>
   <select required name="dispo" id="dispo-select">
    <option value="">--Please choose an option--</option>`


  dispo.forEach((disp) => {

    typeObjString+= `<option value="${disp.id}">${disp.date[2]}/${disp.date[1]}/${disp.date[0]}(${disp.plage})</option>`
  })

  typeObjString+=`</select>  <br/>`
  if(!getToken()) {
    form.appendChild(gsm);
  }
  form.appendChild(label);
  form.appendChild(inputForm)
  form.appendChild(br);
  form.innerHTML+=typeObjString;

  form.appendChild(description);
  form.appendChild(submit);
  form.appendChild(br);
  newDiv.appendChild(form);
  main.appendChild(newDiv);
  newDiv.style.display = "flex";
  newDiv.style.justifyContent = "center";
  newDiv.style.minHeight = "87vh";
  newDiv.style.alignItems = "center";
  newDiv.style.margin = "0";
  newDiv.style.overflow = "hidden";
  newDiv.style.lineHeight = "500%";
  form.style.position = "relative";
  form.style.minHeight = "280px";
  form.style.width = "600px";
  form.style.maxWidth = "100%";
  form.style.backgroundColor = "#f2c491";
  form.style.borderRadius = "10px";
  form.style.boxShadow = "0 8px 24px rgba(0, 32, 63, .45), 0 8px 8px rgba(0, 32, 63, .45)";
  form.style.lineHeight = "2";
  form.addEventListener('submit', async (e)=>{
    e.preventDefault();
    let gsm2=null
    let util=null;
    if (getToken()!=null) {

        const authenticatedUser = await getAuthenticatedUser();
        util = authenticatedUser.id;
    }else{
      gsm2= document.querySelector('#gsm').value;
    }
    const type1 = document.getElementsByName("type")[0].value;
    const dispo1= document.getElementsByName("dispo")[0].value;
    const description1= document.querySelector('#description').value;

    const nbDispo = Number.parseInt(dispo1,10)
    const nbType = Number.parseInt(type1,10)
    const responseDisponibiltite = await fetch(`${process.env.API_BASE_URL}/disponibilite/${nbDispo}`);
    const responseType = await fetch(`${process.env.API_BASE_URL}/objet/typeObjet/${nbType}`);
    const type=await responseType.json();
    const disponibilite = await responseDisponibiltite.json();

    const fileInput = document.querySelector('input[name=file]');
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    const options1 = {
      method: 'POST',
      body: formData
    };
    const responseImage = await fetch(`${process.env.API_BASE_URL}/objet/upload`, options1);

    if (!responseImage.ok) {
      Swal.fire((await responseImage.text()).valueOf())
    }
    const imageSaved =  await responseImage.text();
    const photo= imageSaved

    const options = {
      method: 'POST',
      body: JSON.stringify({
        utilisateur: util,
        gsm: gsm2,
        photo,
        typeObjet:type,
        description :   description1,
        disponibilite
      }
      ),
      headers: {
        'Content-Type': 'application/json',
      },
    };


    const response = await fetch(`${process.env.API_BASE_URL}/objet/ajouterObjet`, options);

    if (!response.ok) {
      Swal.fire((await response.text()).valueOf())
    }
    Navigate("/")
  });
}


async function getTypeObject() {

  const options = {
    method: 'GET',
  };
  const response = await fetch(`${process.env.API_BASE_URL}/objet/typeObjet`,
      options);

  if (!response.ok) {
    throw new Error(
        `fetch error : ${response.status} : ${response.statusText}`);
  }

  const objets = await response.json();
  return objets;
}

async function getDispo() {

  const options = {
    method: 'GET',
  };
  const response = await fetch(`${process.env.API_BASE_URL}/disponibilite`,
      options);

  if (!response.ok) {
    throw new Error(
        `fetch error : ${response.status} : ${response.statusText}`);
  }

  const objets = await response.json();
  return objets;
}

export default AddObjetPage;
