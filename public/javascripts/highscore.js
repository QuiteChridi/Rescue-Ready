let tableBody = document.getElementById("highscoreTableBody")

const players = [
    {name:"Willi", score: 0},
    {name:"Hubert", score: 0},
    {name:"Sepp", score: 0},
    {name:"Fred", score: 0},
    {name:"Paul", score: 0}]


function refreshHighscore(){
    clearTable();
    players.forEach(generateRandomHighscore)
    players.sort((p1,p2) => (p1.score-p2.score));
    players.forEach(addToHighscoreTable)
}

function clearTable(){
    let tableBody = document.getElementById("highscoreTableBody")
    tableBody.innerHTML = "";
}

function generateRandomHighscore(player){
    player.score = Math.floor(Math.random() * 100);
}

function addToHighscoreTable(player){
    let tableBody = document.getElementById("highscoreTableBody")
    row = tableBody.insertRow(0);
    playerNameCell = row.insertCell(0);
    playerScoreCell =row.insertCell(1);

    playerNameCell.innerText = player.name;
    playerScoreCell.innerText = player.score;
}
