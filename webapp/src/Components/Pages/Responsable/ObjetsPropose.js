import {getToken} from "../../../utils/auths";

const tableEnTete = `
  <div style=" justify-content: center; display: flex">
    <table class="tableEnTete">
      <thead> 
        <tr> 
          <th class="objetProposeTh"> Id Objet</th>
          <th class="objetProposeTh"> Type d'objet </th>
          <th class="objetProposeTh"> Photo objet </th>
          <th class="objetProposeTh"> Description objet</th>
          <th class="objetProposeTh"> Accepter/refuser proposition</th>
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


    async function getData() {

        try {
            const data = [];
            const response = await fetch(`${process.env.API_BASE_URL}/objet`);
            let dataHtml = '';
            if (!response.ok) {
                throw new Error('Network response was not ok.');
            }

            const datas = await response.json();

            let index = 0;
            for (let j = 0; j < datas.length; j+=1) {
                if (datas[j].etat === 'proposer') {
                    data[index] = datas[j];
                    index += 1;
                }
            }
            const size = data.length;
            for (let i = 0; i < size; i+=1) {
                    dataHtml += `
                      <tr style="font-family: 'Games', sans-serif;">
                        <td class="objetProposeTd">${data[i].idObjet}</td>
                        <td class="objetProposeTd">${data[i].typeObjet.libelle}</td> 
                        <td class="objetProposeTd"><img src=/api/objet/getPicture/${data[i].idObjet} alt="photo" width="100px"></td> 
                        <td class="objetProposeTd">${data[i].description}</td>
                        <td class="objetProposeTd"><button id="accepter" data-index="${data[i].idObjet}" type="submit" >Accepter</button> <button id="refuser" data-index="${i}" type="submit" >Réfuser</button></td>
                      </tr>
                    `;

            }
            const tableBody = document.querySelector('.tableData');
            tableBody.innerHTML = dataHtml;
            const accepterBouton=document.querySelectorAll('#accepter');
            accepterBouton.forEach(btn=>{
                btn.addEventListener("click",async (event)=>{
                    const val = event.target.getAttribute('data-index');
                    try{
                        const options = {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                Authorization : getToken()
                            },
                        };


                        const rep = await fetch(`${process.env.API_BASE_URL}/objet/accepterObject/${val}`, options);

                        if (!rep.ok) throw new Error(`fetch error : ${rep.status} : ${rep.statusText}`);
                        window.location.reload();
                    }
                    catch (err){
                        throw Error(err);
                    }
                });
            })
            const boutonsRefuser = document.querySelectorAll('#refuser');
            boutonsRefuser.forEach((button) => {
                button.addEventListener("click",  (event) => {
                    const vals = event.target.getAttribute('data-index');
                    const popUp = `
                            <div class="popUpContainer">
                            <div id="formulaireRefuser">
                                <h1>Pourquoi l'objet a été refusé ?</h1>
                                <br>
                                <br>
                                <div id="titreMessage"></div>
                                <form id ="formulaireRefus">
                                    <textarea  placeholder="Votre message" id="reason"></textarea><br>
                                    <input type="submit" value="Envoyer" id="refuseObjet">
                                </form>
                                
                            </div>
                            </div>
                    `;

                    const main = document.querySelector("main");
                    main.insertAdjacentHTML("beforeend", popUp);

                    const titreMessage=document.getElementById("titreMessage");
                    let valeurTitreMessage;
                    if (!data[vals].utilisateur) {
                        valeurTitreMessage = `Le propriétaire n'a pas de compte, écrivez le message qui sera envoyé au : ${data[vals].gsm}`;
                    } else {
                        valeurTitreMessage = `Écrivez le message de refus pour l'utilisateur n° : ${data[vals].utilisateur}`;
                    }
                    titreMessage.innerHTML= valeurTitreMessage;

                    const popup = document.querySelector('.popUpContainer');
                    const form = document.querySelector('#formulaireRefus');
                    form.addEventListener('submit', (e) => {
                        e.preventDefault();
                        console.log("Valeur du textarea: ", document.querySelector('#reason').value);
                        console.log(data[vals])
                        popup.remove();

                    });

                });
            })

        } catch (error) {
            throw new Error(error);
        }
    }

    getData();

}

export default ObjetPropose;