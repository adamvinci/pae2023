import { clearPage } from '../../../utils/render';

const tableEnTete = `
  <div style=" justify-content: center; display: flex">
    <table class="tableEnTete">
      <thead>
        <tr> 
          <th class="th"> Id Utilisateur</th> 
          <th class="th"> Photo objet </th>
          <th class="th"> Description objet</th>
          <th class="th"> Accepter/refuser proposition</th>
          <th class="th"> Date acceptation</th>
        </tr>
      </thead>
      <tbody class="tableData"> 
      </tbody>    
    </table>
  </div>`

function text() {
  const main = document.querySelector('main');
  main.innerHTML = tableEnTete;
}

const ObjetPropose = () => {
  clearPage();
  text();
  table();
  const boutonsRefuser = document.querySelectorAll("#refuser");
  boutonsRefuser.forEach((boutonRefuser) => {
    boutonRefuser.addEventListener("click", event => {
      event.preventDefault();
      const main = document.querySelector("main");
      const popUp = `
      <div class="popUpContainer">
        <div id="formulaireRefuser">
          <h1>Pourquoi l'objet a été refusé ?</h1>
          <br>
          <br>
          <form>
            <textarea  placeholder="Votre message"></textarea><br>
            <input type="submit" value="Envoyer">
          </form>
        </div>
      </div>
      `;
      main.insertAdjacentHTML("beforeend", popUp);
    });
  });
};

function table(){

  const objet1 = { utilisateur: "1", photo:"../../img/stormtrooper.jpg", description: "1un meuble confortable1",dateAcceptation: "11/03/2023"};
  const objet2 = { utilisateur: "2", photo:"../../img/stormtrooper.jpg", description: "2un meuble confortable2",dateAcceptation: "12/03/2023"};
  const objet3 = { utilisateur: "3", photo:"../../img/stormtrooper.jpg", description: "3un meuble confortable3",dateAcceptation: "13/03/2023"};
  const objet4 = { utilisateur: "4", photo:"../../img/stormtrooper.jpg", description: "4un meuble confortable4",dateAcceptation: "14/03/2023"};
  const objet5 = { utilisateur: "5", photo:"../../img/stormtrooper.jpg", description: "5un meuble confortable5",dateAcceptation: "15/03/2023"};
  const objet6 = { utilisateur: "6", photo:"../../img/stormtrooper.jpg", description: "6un meuble confortable6",dateAcceptation: "16/03/2023"};
  const objet7 = { utilisateur: "7", photo:"../../img/stormtrooper.jpg", description: "7un meuble confortable7",dateAcceptation: "17/03/2023"};
  const objet8 = { utilisateur: "8", photo:"../../img/stormtrooper.jpg", description: "8un meuble confortable8",dateAcceptation: "18/03/2023"};
  const objet9 = { utilisateur: "9", photo:"../../img/stormtrooper.jpg", description: "9un meuble confortable9",dateAcceptation: "19/03/2023"};
  const objet10 = { utilisateur: "10", photo:"../../img/stormtrooper.jpg", description: "10un meuble confortable10",dateAcceptation: "20/03/2023"};

  const data = [objet1,objet2, objet3, objet4,objet5,objet6,objet7,objet8,objet9,objet10];
  const tableBody = document.querySelector('.tableData');
  let dataHtml = ' ';
  const size = data.length;

  for (let i = 0; i < size;) {
    dataHtml += `
      <tr style="font-family: 'Games', sans-serif;">
        <td class="td">${data[i].utilisateur}</td> 
        <td class="td"><img src="${data[i].photo}" alt="photo" width="100px"></td> 
        <td class="td">${data[i].description}</td>
        <td class="td"><button id="accepter" type="submit" >Accepter</button> <button id="refuser" type="submit" >Réfuser</button></td>
        <td class="td">${data[i].dateAcceptation}</td>
       </tr>`;
    i += 1
  } 
  tableBody.innerHTML = dataHtml;
}

export default ObjetPropose;