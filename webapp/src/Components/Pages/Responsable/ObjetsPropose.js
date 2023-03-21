const tableEnTete = `
  <div style=" justify-content: center; display: flex">
    <table class="tableEnTete">
      <thead> 
        <tr> 
          <th class="objetProposeTh"> Id Utilisateur</th> 
          <th class="objetProposeTh"> Photo objet </th>
          <th class="objetProposeTh"> Description objet</th>
          <th class="objetProposeTh"> Accepter/refuser proposition</th>
          <th class="objetProposeTh"> Date acceptation</th>
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


};

function table() {


    let data;

    async function getData() {
        try {

            const response = await fetch(`${process.env.API_BASE_URL}/objet`);
            let dataHtml = ' ';
            if (!response.ok) {
                throw new Error('Network response was not ok.');
            }

            const datas = await response.json();
            data = datas;

            const size = data.length;

            for (let i = 0; i < size;) {
                dataHtml += `
                  <tr style="font-family: 'Games', sans-serif;">
                    <td class="objetProposeTd">${data[i].idObjet}</td> 
                    <td class="objetProposeTd"><img src=/api/objet/getPicture/${data[i].idObjet} alt="photo" width="100px"></td> 
                    <td class="objetProposeTd">${data[i].description}</td>
                    <td class="objetProposeTd"><button id="accepter" type="submit" >Accepter</button> <button id="refuser" type="submit" >Réfuser</button></td>
                    <td class="objetProposeTd">${data[i].date_acceptation}</td>
                  </tr>
                `;
                i += 1

            }
            const tableBody = document.querySelector('.tableData');
            tableBody.innerHTML = dataHtml;
            const boutonsRefuser = document.querySelectorAll('#refuser');
            boutonsRefuser.forEach((button) => {
                button.addEventListener("click", event => {
                    event.preventDefault();
                    console.log("refuser");
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
            })

        } catch (error) {
            throw new Error(error);
        }
    }

    getData();

}

export default ObjetPropose;