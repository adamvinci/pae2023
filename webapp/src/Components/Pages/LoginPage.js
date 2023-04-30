import Swal from "sweetalert2";
import {
  getRememberMe,
  setAuthenticatedUser,
  setRememberMe
} from '../../utils/auths';
import {clearPage} from '../../utils/render';
import Navbar from '../Navbar/Navbar';
import Navigate from '../Router/Navigate';

const LoginPage = () => {
  clearPage();

  renderRegisterForm();
};

function renderRegisterForm() {
  const main = document.querySelector('main');
  main.innerHTML = `<div id="login-container">
  <form id="login-form" class="p-5">
    <h1 id="login-title">Bienvenue dans la page de connexion</h1>
    <input type="text" id="email-input" class="form-control mb-3" placeholder="Email" required>
    <input type="password" id="password-input" class="form-control mb-3" placeholder="Password" required>
    <div id="remember-me-wrapper" class="mb-3 form-check">
      <input type="checkbox" id="remember-me" class="form-check-input">
      <label for="remember-me" class="form-check-label">Remember me</label>
    </div>
    <input type="submit" id="login-submit" class="btn btn-info" value="Login">
    <br>
    <a id="register-link" class="text-dark mt-3" style="cursor: pointer;">Pas encore inscrit? Inscrivez-vous ici!</a>
  </form>
</div>`
  const rememberme = document.querySelector("#remember-me")
  const remembered = getRememberMe();
  rememberme.checked = remembered;
  rememberme.addEventListener('click', onCheckboxClicked);

  const connecterLink = document.querySelector('#register-link');
  connecterLink.addEventListener("click", () => {
    Navigate("/register")
  });

  const form = document.getElementById("login-form");
  form.addEventListener('submit', onLogin);
}

function onCheckboxClicked(e) {
  setRememberMe(e.target.checked);
}

async function onLogin(e) {
  e.preventDefault();

  const email = document.querySelector('#email-input').value;
  const password = document.querySelector('#password-input').value;

  const options = {
    method: 'POST',
    body: JSON.stringify({
      email,
      password,
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const response = await fetch(`${process.env.API_BASE_URL}/auths/login`,
      options);

  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  const authenticatedUser = await response.json();

  setAuthenticatedUser(authenticatedUser);

  Navbar();

  Navigate('/');
}

export default LoginPage;
