
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
  </div>`;

function text() {
  const main = document.querySelector('main');
  main.innerHTML = tableEnTete;
}

const ObjetPropose = () => {
  text();
  table();
  const boutonsRefuser = document.getElementById('refuser');
  
  boutonsRefuser.addEventListener("click", event => {
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
};

function table(){


  let data;

  

  async function getData() {
    try {

      const response = await fetch(`${process.env.API_BASE_URL}/objet`);
      let dataHtml = ' ';
      if (!response.ok) {
        throw new Error('Network response was not ok.');
      }

      const datas = await response.json();
      data=datas;
      
      const size = data.length;

      for (let i = 0; i < size;) {
        dataHtml += `
          <tr style="font-family: 'Games', sans-serif;">
            <td class="td">${data[i].idObjet}</td> 
            <td class="td"><img src=/api/objet/getPicture/${data[i].idObjet} alt="photo" width="100px"></td> 
            <td class="td">${data[i].description}</td>
            <td class="td"><button id="accepter" type="submit" >Accepter</button> <button id="refuser" type="submit" >Réfuser</button></td>
            <td class="td">${data[i].date_acceptation}</td>
          </tr>`;
        i += 1
      } 
      const tableBody = document.querySelector('.tableData');
      tableBody.innerHTML = dataHtml;
    } catch (error) {
      throw new Error(error);    }
  }
  
  getData();
  
}

export default ObjetPropose;