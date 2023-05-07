import Swal from "sweetalert2";
import Navigate from "../Router/Navigate";

const HomePage = async () => {
  const main = document.querySelector('main');
  main.innerHTML = `<h3 class ="titles">Loading ... </h3>`;
  let objets = await getObjects();
  console.log(objets);
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
    if(filteredArray.length===0){
      Swal.fire({
        title: `Pas d'objets ${etat} en magasin`,
        confirmButtonText: 'Acceuil',
      }).then(() => {
        window.location.href='/';
      })
    }
  }
  if(objets.length === 0)  main.innerHTML = `<br><h3 class = "titles" >Pas encore d'objets ! </h3>`;
  // Filter the array depending on the type (if there are one and the type is not 'None')
  if (type && parseInt(type, 10) !== 0) {
    const filteredArray2 = objets.filter(
        (o) => o.typeObjet.libelle.startsWith(type));
    objets = filteredArray2;
    if(filteredArray2.length===0){
      Swal.fire({
        title: 'Pas d\'objets de ce type !',
        confirmButtonText: 'Acceuil',
      }).then(() => {
        window.location.href='/';
      })
    }
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
<div id="carouselExampleControls" class="carousel carousel-dark slide"  data-bs-ride="carousel" data-bs-interval="10000000">
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img id = "imgCarousel" class="d-block " src=/api/objet/getPicture/${objets[0].idObjet}  data-x = ${0} alt=${objets[0].descritpion} >
            <div class="carousel-caption d-none d-md-block">
        <h5 >${objets[0].typeObjet.libelle}</h5>
        <p >${changeEtatName(objets[0].etat)}</p>
      </div>
    </div>

    `
const objets2 = objets

  objets2.slice(1).forEach((o)=>{
      html += `      
         <div class="carousel-item">
      <img id = "imgCarousel" class="d-block  " src=/api/objet/getPicture/${o.idObjet} data-x = ${objets.indexOf(o)} alt=${o.description} >
               <div class="carousel-caption d-none d-md-block">
        <h5 id="libelle">${o.typeObjet.libelle}</h5>

        <p id = "etat">
  ${changeEtatName(o.etat)}
</p>
      </div>
    </div>
      `
})

 html +=`
  </div>
  <button id="btnCaroussel" class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Previous</span>
  </button>
  <button id="btnCaroussel" class="carousel-control-next" type="button" data-bs-target="#carouselExampleControls" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Next</span>
  </button>
</div>
</div>
</div>

  `;


  main.innerHTML = html;
  let popup;
  const img = document.querySelectorAll("#imgCarousel");
  img.forEach((im)=>{
    im.addEventListener('click',(e)=>{
      const id = e.target.dataset.x
      const objData = objets[id]
 popup = `
  <div class="popUpContainer">
        <div id="informationContainer" style="overflow-x: scroll;">
          <div class="photo"> 
            <h1>Photo</h1>
            <img src="/api/objet/getPicture/${objData.idObjet}" alt="Photo" width="100px">
          </div>
          <div class="description">
            <h1>Description</h1>
            <p>${objData.description}</p>
          </div>
          <div class = "detail">
           <table>
              <thead> 
                <tr> 
                  <th class="rechercheObjetsTh"> Localisation </th> 
                  <th class="rechercheObjetsTh"> Type d'objet </th> 
                  <th class="rechercheObjetsTh"> Date dépot </th>
                  <th class="rechercheObjetsTh"> Date vente </th>
                  <th class="rechercheObjetsTh"> Prix de l'objet </th>
                  <th class="rechercheObjetsTh"> Etat de vente </th>
                </tr>
              </thead>
              <tbody class="tableData">
                <tr>
                  <td class="rechercheObjetsTd">${objData.localisation}</td> 
                  <td class="rechercheObjetsTd">${objData.typeObjet.libelle}</td>
                  <td class="rechercheObjetsTd">Le ${objData.date_depot[2]}/${objData.date_depot[1]}/${objData.date_depot[0]}  </td>
                  <td class="rechercheObjetsTd">${objData.date_vente ? ` Le ${objData.date_vente[2]}/${objData.date_vente[1]}/${objData.date_vente[0]} ` : '/'}</td>
                  <td class="rechercheObjetsTd">${objData.prix
     ? `${objData.prix}  € ` : '/'} </td>
                  <td class="rechercheObjetsTd">${objData.etat}</td>
                </tr>
              </tbody>    
            </table>
          </div>
          <div class = "fermer"></div>
          <input type="button" id="closeButton" value="Fermer">
        </div>
       
      </div>

    `
      const btn=document.querySelectorAll("#btnCaroussel");
      btn.forEach((btnn)=>{
        const b = btnn
        b.style.display = "none"
        b.onclick = "return false"
      })
      main.insertAdjacentHTML("beforeend", popup);
      const popupContainer = document.querySelector('.popUpContainer');
      const closeButton = popupContainer.querySelector('#closeButton');
      closeButton.addEventListener('click', closePopup);
    })
  })


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
function closePopup() {
  const popup = document.querySelector('.popUpContainer');
  if (popup) {

    popup.parentNode.removeChild(popup);
  }
  const btn=document.querySelectorAll("#btnCaroussel");
  btn.forEach((btnn)=>{
    const b = btnn
    b.style.display = "flex"
  })
}

function changeEtatName(etat){
 if (etat === 'accepte') {
   return  'Accepté'
 } if (etat === 'refuser') {
    return 'Refusé'
 }  if (etat === 'en vente') {
   return  'En Vente'
}  if (etat === 'vendu') {
   return 'Vendu'
 }
   return  'Proposé'


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
  const response = await fetch(`${process.env.API_BASE_URL}/typeObjet`,
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

