import { getRememberMe, setRememberMe } from '../../utils/auths';
import { clearPage, renderPageTitle } from '../../utils/render';
import Navbar from '../Navbar/Navbar';
import Navigate from '../Router/Navigate';


const RegisterPage = () => {
  clearPage();
  renderPageTitle('Register');
  renderRegisterForm();
};

function renderRegisterForm() {
  const main = document.querySelector('main');
  const newDiv = document.createElement("div");
  const form = document.createElement('form');
  form.className = 'p-5';
  const title = document.createElement('h1');
  title.innerText="Bienvenue dans la page d'inscription";
  title.style.color="#634835";
  const email = document.createElement('input');
  email.type = 'text';
  email.id = 'email';
  email.placeholder = 'email';
  email.required = true;
  email.className = 'form-control mb-3';
  const nom = document.createElement('input');
  nom.type = 'text';
  nom.id = 'nom';
  nom.placeholder = 'nom';
  nom.required = true;
  nom.className = 'form-control mb-3';
  const prenom = document.createElement('input');
  prenom.type = 'text';
  prenom.id = 'prenom';
  prenom.placeholder = 'prenom';
  prenom.required = true;
  prenom.className = 'form-control mb-3';
  const gsm = document.createElement('input');
  gsm.type = 'text';
  gsm.id = 'gsm';
  gsm.placeholder = 'gsm';
  gsm.required = true;
  gsm.className = 'form-control mb-3';
  const password = document.createElement('input');
  password.type = 'password';
  password.id = 'password';
  password.required = true;
  password.placeholder = 'password';
  password.className = 'form-control mb-3';
  const submit = document.createElement('input');
  submit.value = 'register';
  submit.type = 'submit';
  submit.className = 'btn btn-info';

  const formCheckWrapper = document.createElement('div');
  formCheckWrapper.className = 'mb-3 form-check';

  const rememberme = document.createElement('input');
  rememberme.type = 'checkbox';
  rememberme.className = 'form-check-input';
  rememberme.id = 'rememberme';
  const remembered = getRememberMe();
  rememberme.checked = remembered;
  rememberme.addEventListener('click', onCheckboxClicked);

  const checkLabel = document.createElement('label');
  checkLabel.htmlFor = 'rememberme';
  checkLabel.className = 'form-check-label';
  checkLabel.textContent = 'Remember me';

  formCheckWrapper.appendChild(rememberme);
  formCheckWrapper.appendChild(checkLabel);
  form.appendChild(title);
  form.appendChild(nom);
  form.appendChild(prenom);
  form.appendChild(email);
  form.appendChild(password);
  form.appendChild(gsm)
  form.appendChild(formCheckWrapper);
  form.appendChild(submit);
  newDiv.appendChild(form);
  main.appendChild(newDiv);
  newDiv.style.display="flex";
  newDiv.style.justifyContent="center";
  newDiv.style.minHeight="87vh";
  newDiv.style.alignItems="center";
  newDiv.style.margin="0";
  newDiv.style.overflow="hidden";
  newDiv.style.lineHeight="500%";
  form.style.position="relative";
  form.style.minHeight="280px";
  form.style.width="600px";
  form.style.maxWidth="100%";
  form.style.backgroundColor="#f2c491";
  form.style.borderRadius="10px";
  form.style.boxShadow="0 8px 24px rgba(0, 32, 63, .45), 0 8px 8px rgba(0, 32, 63, .45)";
  form.style.lineHeight="2";
  form.addEventListener('submit', onRegister);
}

function onCheckboxClicked(e) {
  setRememberMe(e.target.checked);
}

async function onRegister(e) {
  e.preventDefault();

  const nom = document.querySelector('#nom').value;
  const prenom = document.querySelector('#prenom').value;
  const email = document.querySelector('#email').value;
  const password = document.querySelector('#password').value;
  const gsm = document.querySelector('#gsm').value;

  const options = {
    method: 'POST',
    body: JSON.stringify({
      email,
      password,
      nom,
      prenom,
      gsm,
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/auths/register`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);

  await response.json();

  Navbar();

  Navigate('/login');
}

export default RegisterPage;
