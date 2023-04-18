import Swal from "sweetalert2";
import {clearPage} from '../../utils/render';
import Navbar from '../Navbar/Navbar';
import Navigate from '../Router/Navigate';

import avatar1 from '../../img/avatar1.png'
import avatar2 from '../../img/avatar2.jpg'

const RegisterPage = () => {
  clearPage();


  renderRegisterForm();
};



function renderRegisterForm() {
  const main = document.querySelector('main');
  const newDiv = document.createElement("div");
  newDiv.id = "divForm"
  const form = document.createElement('form');
  form.id = "form"
  form.className = 'p-5';
  const title = document.createElement('h1');
  title.innerText = "Creer votre compte";
  title.style.color = "#634835";
  const email = document.createElement('input');
  email.type = 'text';
  email.id = 'email';
  email.placeholder = 'Email';
  email.required = true;
  email.className = 'form-control mb-3';
  const nom = document.createElement('input');
  nom.type = 'text';
  nom.id = 'nom';
  nom.placeholder = 'Nom';
  nom.required = true;
  nom.className = 'form-control mb-3';
  const prenom = document.createElement('input');
  prenom.type = 'text';
  prenom.id = 'prenom';
  prenom.placeholder = 'Prenom';
  prenom.required = true;
  prenom.className = 'form-control mb-3';
  const gsm = document.createElement('input');
  gsm.type = 'text';
  gsm.id = 'gsm';
  gsm.placeholder = 'GSM';
  gsm.required = true;
  gsm.className = 'form-control mb-3';
  const password = document.createElement('input');
  password.type = 'password';
  password.id = 'password';
  password.required = true;
  password.placeholder = 'Password';
  password.className = 'form-control mb-3';
  const submit = document.createElement('input');
  submit.value = 'register';
  submit.type = 'submit';
  submit.className = 'btn btn-info';

  const formCheckWrapper = document.createElement('div');
  formCheckWrapper.className = 'mb-3 form-check';

  const msgErreur = document.createElement("h4");
  msgErreur.id = "msgErreur";
  msgErreur.innerText = "";
  msgErreur.style.color = "red";

  const connecterLink = document.createElement("a");
  connecterLink.id = "connecterLink";
  connecterLink.style.cursor = "pointer"
  connecterLink.innerText = "deja inscrit? connectez-vous ici!";
  connecterLink.className = "text-dark mt-3";
  connecterLink.addEventListener("click", () => {
    Navigate("/login")
  })

  form.appendChild(title);
  form.appendChild(nom);
  form.appendChild(prenom);
  form.appendChild(email);
  form.appendChild(password);
  form.appendChild(gsm);
  const form1 = document.createElement('form');
  const labelForm1 = document.createElement('label')
  labelForm1.innerText = "Select File"
  const inputForm = document.createElement('input')
  inputForm.type = "file"
  inputForm.accept ="image/*"
  inputForm.name = "file"
  inputForm.required = "true"
  form1.appendChild(labelForm1)
  form1.appendChild(inputForm);
  form1.style.display = "none"
  form.innerHTML +=`
  <label>
    <input type="radio" name="pictureType" value="upload">
  Use file
  </label>
  
  <label>
    <input type="radio" name="pictureType" value="avatar" checked>
    Use avatar
  </label>
  <div id="radioAvatar">
    <label id ="firstImg" class="selected">
      <input type="radio" name="avatar" id = "avatar1.png" value="${avatar1}" style="display:none;" checked>
      <img src="${avatar1}" alt="avatar1">
    </label>
    <label id="secondImg" ">
      <input type="radio" name="avatar" id = "avatar2.jpg" value="${avatar2}" style="display:none;">
      <img src="${avatar2}" alt="avatar2">
    </label>
  </div>

`


  form.appendChild(form1);
  form.appendChild(formCheckWrapper);
  form.appendChild(submit);
  form.appendChild(msgErreur)
  form.appendChild(connecterLink)
  newDiv.appendChild(form);
  main.appendChild(newDiv);

  const labels = document.querySelectorAll('#radioAvatar label');
  labels.forEach(label => {
    label.addEventListener('click', () => {
      labels.forEach(otherLabel => {
        otherLabel.classList.remove('selected');
      });
      label.classList.add('selected');
    });
  });


  const uploadRadio = document.querySelector('input[value="upload"]');
  const avatarRadio = document.querySelector('input[value="avatar"]');
  const divRadio = document.querySelector("#radioAvatar")
  avatarRadio.addEventListener('change', () => {
    form1.style.display = 'none';
    divRadio.style.display = 'block'
  });
  uploadRadio.addEventListener('change', () => {
    form1.style.display = 'block';
    divRadio.style.display = 'none'
  });
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
  form.addEventListener('submit', onRegister);

}

async function onRegister(e) {
  e.preventDefault();

  const nom = document.querySelector('#nom').value;
  const prenom = document.querySelector('#prenom').value;
  const email = document.querySelector('#email').value;
  const password = document.querySelector('#password').value;
  const gsm = document.querySelector('#gsm').value;

  const fileInput = document.querySelector('input[name=file]');

  const uploadRadio = document.querySelector('input[name="pictureType"][value="upload"]');


  let image ;
  if(uploadRadio.checked){
    const formData = new FormData();
    image=fileInput.files[0].name;
    formData.append('file', fileInput.files[0]);
    const options1 = {
      method: 'POST',
      body: formData
    };
    fetch(`${process.env.API_BASE_URL}/auths/upload`, options1);
  }else{
    const avatarRadios = document.getElementsByName('avatar');
    avatarRadios.forEach((avtr)=>{
      if(avtr.checked){

        image = avtr.id;
      }
    });

  }



  const options = {
    method: 'POST',
    body: JSON.stringify({
      email,
      password,
      nom,
      prenom,
      gsm,
      image
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/auths/register`,
      options);

  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  await response.json();
  Navbar();

  Navigate('/login');

}



export default RegisterPage;