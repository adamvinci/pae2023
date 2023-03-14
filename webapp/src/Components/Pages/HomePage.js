const HomePage =async () => {
  const main = document.querySelector('main');
  main.innerHTML = `<h3>Welcome to your home page!</h3>`;
  const objets = await getObjects();
  shuffleArray(objets);
  console.log(objets);

  let html = "<div id=page> <div id=rightC> trier par type</div> <div id=container>";
  /*
    const response = await fetch(`${process.env.API_BASE_URL}/objet/getPicture/${objets[2].idObjet}`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);

  const img1 = await response.blob();

  img.src = URL.createObjectURL(img1);

  */
  objets.forEach((e)=>{
    html += `
<div class = "container2">
<div id = "image">
 <img src=/api/objet/getPicture/${e.idObjet} alt=${e.description}  data-id = ${e.idObjet} width="200" height="200">
 <div class = "title">${e.typeObjet.libelle}</div>
  <div class = "subtitle">${e.description}</div>
</div>
</div>
`
  })
  html += '</div></div>'

  main.innerHTML = html;




  const x = document.querySelectorAll('img');
  x.forEach((immg)=>{
    immg.addEventListener("click",(e)=>{
      console.log("img",e.target.dataset.id)
    })
  })

}


async function getObjects(){

  const options = {
    method: 'GET',

  };
  const response = await fetch(`${process.env.API_BASE_URL}/objet`, options);

  if (!response.ok) throw new Error(`fetch error : ${response.status} : ${response.statusText}`);

  const objets = await response.json();
  return objets;
}

function shuffleArray(array) {
  const aray = array
  for (let i = aray.length - 1; i > 0; i-=1) {
    const j = Math.floor(Math.random() * (i + 1));
    [aray[i], aray[j]] = [aray[j], aray[i]];
  }
  return aray;
}

export default HomePage;

