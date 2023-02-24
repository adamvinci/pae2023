import { getRememberMe, setAuthenticatedUser, setRememberMe } from '../../utils/auths';
import { clearPage } from '../../utils/render';
import Navbar from '../Navbar/Navbar';
import Navigate from '../Router/Navigate';

const LoginPage = () => {
  clearPage();

  renderRegisterForm();
};


  function renderRegisterForm() {
    const main = document.querySelector('main');
    const form = document.createElement('form');
    form.className = 'p-5';
    const title = document.createElement('h1');
    title.innerText="Bienvenue dans la page de connexion";
    title.style.color="#634835";
    const email = document.createElement('input');
    email.type = 'text';
    email.id = 'email';
    email.placeholder = 'email';
    email.required = true;
    email.className = 'form-control mb-3';
    const password = document.createElement('input');
    password.type = 'password';
    password.id = 'password';
    password.required = true;
    password.placeholder = 'password';
    password.className = 'form-control mb-3';
    const submit = document.createElement('input');
    submit.value = 'Login';
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
    form.appendChild(email);
    form.appendChild(password);
    form.appendChild(formCheckWrapper);
    form.appendChild(submit);
    main.appendChild(form);
    main.style.background="#ecdec5"
    main.style.display="flex";
    main.style.justifyContent="center";
    main.style.minHeight="100vh";
    main.style.alignItems="center";
    main.style.margin="0";
    main.style.overflow="hidden";
    main.style.lineHeight="500%";
    form.style.position="relative";
    form.style.minHeight="480px";
    form.style.width="780px";
    form.style.maxWidth="100%";
    form.style.backgroundColor="#f2c491";
    form.style.borderRadius="10px";
    form.style.boxShadow="0 8px 24px rgba(0, 32, 63, .45), 0 8px 8px rgba(0, 32, 63, .45)";
    form.style.lineHeight="2";
    form.addEventListener('submit', onLogin);
  }

  function onCheckboxClicked(e) {
    setRememberMe(e.target.checked);
  }

  async function onLogin(e) {
    e.preventDefault();

    const email = document.querySelector('#email').value;
    const password = document.querySelector('#password').value;

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


  const response = await fetch(`${process.env.API_BASE_URL}/auths/login`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);

  const authenticatedUser = await response.json();

  console.log('Authenticated user : ', authenticatedUser);

  setAuthenticatedUser(authenticatedUser);

  Navbar();

  Navigate('/');
}

export default LoginPage;
