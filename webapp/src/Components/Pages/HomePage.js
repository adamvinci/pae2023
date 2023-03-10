const HomePage =async () => {
  const main = document.querySelector('main');
  main.innerHTML = `<h3>Welcome to your home page!</h3>`;
  const objets = await getObjects();
  console.log(objets);

  const img = document.createElement("img");
  img.src = `/api/objet/getPicture/${objets[2].idObjet}`
  main.append(img);

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
export default HomePage;

