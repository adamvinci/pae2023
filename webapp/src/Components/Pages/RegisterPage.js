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
  main.innerHTML = `<div id="divForm">
  <form id="form" class="p-5">
    <h1 style="color: #634835;">Creer votre compte</h1>
    <input type="text" id="email" placeholder="Email" required class="form-control mb-3">
    <input type="text" id="nom" placeholder="Nom" required class="form-control mb-3">
    <input type="text" id="prenom" placeholder="Prenom" required class="form-control mb-3">
    <input type="text" id="gsm" placeholder="GSM" required class="form-control mb-3">
    <input type="password" id="password" placeholder="Password" required class="form-control mb-3">
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
   <input id="reg" type="submit" value="register" class="btn btn-info">
    <div class="mb-3 form-check">
      <h4 id="msgErreur" style="color: red;"></h4>
    </div>
    <a id="connecterLink" style="cursor: pointer;" class="text-dark mt-3">deja inscrit? connectez-vous ici!</a>
  </form>
</div>`
  const connecterLink = document.querySelector("#connecterLink");
  connecterLink.addEventListener("click", () => {
    Navigate("/login")
  })
  const reg = document.getElementById("reg")
  const form = document.querySelector('#form')
  const form1 = document.createElement('form');
  const labelForm1 = document.createElement('label')
  labelForm1.innerText = "Select File"
  const inputForm = document.createElement('input')
  inputForm.type = "file"
  inputForm.accept = "image/*"
  inputForm.name = "file"
  inputForm.required = "true"
  form1.appendChild(labelForm1)
  form1.appendChild(inputForm)
  form1.style.display = "none"
  reg.parentNode.insertBefore(form1, reg)

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

  const uploadRadio = document.querySelector(
      'input[name="pictureType"][value="upload"]');

  let image;
  if (uploadRadio.checked) {
    const formData = new FormData();
    image = fileInput.files[0].name;
    formData.append('file', fileInput.files[0]);
    const options1 = {
      method: 'POST',
      body: formData
    };
    const responseImage = await fetch(
        `${process.env.API_BASE_URL}/auths/upload`, options1);

    if (!responseImage.ok) {
      Swal.fire((await responseImage.text()).valueOf())
    }
    const imageSaved = await responseImage.text();
    image = imageSaved
    console.log(image)

  } else {
    const avatarRadios = document.getElementsByName('avatar');
    avatarRadios.forEach((avtr) => {
      if (avtr.checked) {
        console.log(avtr.id);
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