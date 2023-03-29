import Swal from "sweetalert2";
import Navigate from "../Router/Navigate";

const HomePage = async () => {
  const main = document.querySelector('main');
  main.innerHTML = `<h3>Welcome to your home page!</h3>`;
  let objets = await getObjects();
  shuffleArray(objets);

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
  });

  html += `</div>
<div id = "carousel">
<div id="carouselExampleControls" class="carousel carousel-dark slide" data-bs-ride="carousel">
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img id = "imgCarousel" class="d-block " src=/api/objet/getPicture/${objets[0].idObjet}  alt=${objets[0].descritpion} >
            <div class="carousel-caption d-none d-md-block">
        <h5 >${objets[0].typeObjet.libelle}</h5>
        <p >${objets[0].etat}</p>
      </div>
    </div>

    `


      objets.slice(1).forEach((o)=>{
      html += `      
         <div class="carousel-item">
      <img id = "imgCarousel" class="d-block  " src=/api/objet/getPicture/${o.idObjet} alt=${o.description} >
               <div class="carousel-caption d-none d-md-block">
        <h5 id="libelle">${o.typeObjet.libelle}</h5>
        <p id = "etat">${o.etat}</p>
      </div>
    </div>
      `
})

 html +=`
  </div>
  <button id = "prevv" class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleControls" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Next</span>
  </button>
</div>
</div>
</div>

  `;



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

