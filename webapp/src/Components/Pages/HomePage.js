import Swal from "sweetalert2";
import Navigate from "../Router/Navigate";

const HomePage = async () => {
  const main = document.querySelector('main');
  main.innerHTML = `<h3>Welcome to your home page!</h3>`;
  let objets = await getObjects();
  shuffleArray(objets);
  console.log(objets);

  let etat = window.location.search.split("?")[1]?.split("=")[1];

  const type = window.location.search.split("?")[2]?.split("=")[1];

  // Filter the array depending on the state (if there are one)
  if (etat) {
    if (etat === "vente") {
      etat = "en vente"
    }
    const filteredArray = objets.filter((o) => o.etat === etat);
    objets = filteredArray;
  }
  // Filter the array depending on the type (if there are one and the type is not 'None')
  if (type && parseInt(type, 10) !== 0) {
    const filteredArray2 = objets.filter(
        (o) => o.typeObjet.libelle.startsWith(type));
    objets = filteredArray2;
  }
  const typeObjet = await getTypeObject();

  let html = `<div id=page> <div id=rightC> <label>  <div id = typeObjet> <input type= radio  name  = menu data-value=0>None  </label> </div>  `
  typeObjet.forEach((typee) => {
    html += `
  <div id = "typeObjet">
          <label>
          <input type="radio" name="menu" data-value=${typee.libelle}>
          ${typee.libelle}
          </label>
  </div>
`
  })
  html += "</div><div id=container>";
  objets.forEach((e) => {
    html += `
<div class = "container2">
<div id = "image">
 <img src=/api/objet/getPicture/${e.idObjet} alt=${e.description}  data-id = ${e.idObjet} width="200" height="200">
 <div class = "title">${e.typeObjet.libelle}</div>
  <div class = "subtitle">${e.etat}</div>
  <div class="hide">
  <div class ="hideContent">
  
  <p>Description : ${e.description}</p>
  ${e.prix ? `<p>Prix : ${e.prix}</p>` : ''}
  ${e.date_vente
        ? `<p> Date de vente : Le ${e.date_vente[2]}/ ${e.date_vente[1]}/${e.date_vente[0]} </p>`
        : ''}
  </div>
</div>
</div>
</div>
`
  })
  html += '</div></div>'

  main.innerHTML = html;

  const t = document.querySelectorAll("input");
  t.forEach((inpt) => {
    if (inpt.dataset.value === type) {
      /* eslint-disable */
      inpt.checked = true;
      /* eslint-enable */
    }

    inpt.addEventListener("input", (e) => {
      e.preventDefault();
      if (etat) {
        if (etat === "en vente") {
          etat = "vente"
        }
        Navigate(`/?etat=${etat}?type=${e.target.dataset.value}`)
      } else {
        Navigate(`/?etat=?type=${e.target.dataset.value}`)
      }

    })
  })

}

async function getObjects() {

  const options = {
    method: 'GET',
  };
  const response = await fetch(`${process.env.API_BASE_URL}/objet/storeObject`, options);
  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  const objets = await response.json();
  return objets;
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

function shuffleArray(array) {
  const aray = array
  for (let i = aray.length - 1; i > 0; i -= 1) {
    const j = Math.floor(Math.random() * (i + 1));
    [aray[i], aray[j]] = [aray[j], aray[i]];
  }
  return aray;
}

export default HomePage;

