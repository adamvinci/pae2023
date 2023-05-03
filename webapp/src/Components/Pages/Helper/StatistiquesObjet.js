
import Swal from "sweetalert2";
import Chart from 'chart.js';
import {clearPage} from "../../../utils/render";
import {getToken} from "../../../utils/auths";
import Navigate from "../../Router/Navigate";

const Statistique = async () => {
  clearPage();
  const main = document.querySelector('main');
  const objets = await getObjects();
  const nbObjetsVendu = objets.filter((o) => o.etat === 'vendu').length
  const nbObjetsAccepte = objets.filter((o) => o.etat === 'accepte').length
  const nbObjetsRefuser = objets.filter((o) => o.etat === 'refuser').length

  main.innerHTML += `
  <div id ="graph">
  
  <div id ="graph1">
  <h5>Répartition des ${objets.length} objets proposé par les clients selon leur état</h5>
  <canvas id="myChart"></canvas>
</div>

<div id = graph2>
  <select id="yearSelect">
  <option value="">Select a year</option>
</select>
  <h5>Répartition des  objets proposé par période</h5>
  <canvas id="myChart2"></canvas>

</div>
</div>

`
  let date = window.location.search.split("=")[1];
  if (!date) {
    date = 2022
  }

  const ctx = document.getElementById("myChart");

  const mychart = new Chart(ctx, {
    type: "doughnut",
    data: {
      labels: ['Objets accepté', 'Objets refusé', 'Objets vendu'],
      datasets: [{
        backgroundColor: [
          'rgb(255, 99, 132)',
          'rgb(54, 162, 235)',
          'rgb(255, 205, 86)'
        ],
        data: [nbObjetsAccepte, nbObjetsRefuser, nbObjetsVendu]
      }]
    },
    options:{
      responsive:true,
      maintainAspectRatio: false,
    }

  });
  const ctx2 = document.getElementById("myChart2");
  const monthCounts = {
    Janvier: 0,
    Fevrier: 0,
    Mars: 0,
    Avril: 0,
    Mai: 0,
    Juin: 0,
    Juillet: 0,
    Aout: 0,
    Septembre: 0,
    Octobre: 0,
    Novembre: 0,
    Decembre: 0
  };
  const years = new Set();
  objets.forEach((o) => {
    years.add(o.disponibilite.date[0]);
    if (o.disponibilite.date[0] === Number.parseInt(date, 10)) {
      const monthIndex = o.disponibilite.date[1] - 1; // Month index is 0-based
      const month = Object.keys(monthCounts)[monthIndex]; // Get the month name based on index
      monthCounts[month] += 1;
    }
  })

  const yearSelect = document.getElementById('yearSelect');
  years.forEach((year) => {
    const option = document.createElement('option');
    option.value = year;
    option.textContent = year;
    if(option.value === date){
      option.selected = true
    }
    yearSelect.appendChild(option);
  })

  yearSelect.addEventListener('change', () => {
    const selectedYear = yearSelect.value;
    Navigate(`/Statistiques?year=${selectedYear}`)
  });
  const barColors = ["red", "green", "blue", "orange", "brown", "purple",
    "pink", "teal", "gray", "yellow", "cyan", "magenta", "lime"];

  const mychart2 = new Chart(ctx2, {
    type: "bar",
    data: {
      labels: ['Janvier', 'Fevrier', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet',
        'Aout', 'Septembre', 'Octobre', 'Novembre', 'Decembre'],
      datasets: [{
        label: date,
        backgroundColor: barColors,
        data: [monthCounts.Janvier,
          monthCounts.Fevrier,
          monthCounts.Mars,
          monthCounts.Avril,
          monthCounts.Mai,
          monthCounts.Juin,
          monthCounts.Juillet,
          monthCounts.Aout,
          monthCounts.Septembre,
          monthCounts.Octobre,
          monthCounts.Novembre,
          monthCounts.Decembre]
      }]
    },

    options:{
      responsive:true
    }
  });
};

async function getObjects() {

  const opt = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: getToken()
    },
  };
  const response = await fetch(`${process.env.API_BASE_URL}/objet`, opt);

  if (!response.ok) {
    Swal.fire((await response.text()).valueOf())
  }

  const datas = await response.json();
  return datas
}

export default Statistique;