function setJoker(newJokers, id){
    return fetch("/setJoker", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({
            newAmountOfJokers: newJokers,
            jokerId: id
        })
    })
        .then(response => response.json())
        .catch(error => console.error("Fehler beim Setzen der neuen Anzahl Joker:", error));
}


function getFiftyFiftyJoker(){
    return fetch("/getFiftyFiftyJoker", {
        method:"GET",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include"
    })
        .then(response => response.json())
        .catch(error => console.error("Fehler beim Abrufen der verfügbaren FiftyFifty-Joker:", error));
}

function setFiftyFiftyJoker(newJokers) {
    return fetch("/setFiftyFiftyJoker", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({newAmountOfJokers: newJokers})
    })
        .then(response => response.json())
        .catch(error => console.error("Fehler beim Setzen der neuen Anzahl FiftyFiftyJoker:", error));
}

function getPauseJoker(){
    return fetch("/getPauseJoker", {
        method:"GET",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include"
    })
        .then(response => response.json())
        .catch(error => console.error("Fehler beim Abrufen der verfügbaren Pause-Joker:", error));
}


function setPauseJoker(newJokers) {
    return fetch("/setPauseJoker", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({newAmountOfJokers: newJokers})
    })
        .then(response => response.json())
        .catch(error => console.error("Fehler beim Setzen der neuen Anzahl PauseJoker:", error));
}

function getDoublePointsJoker(){
    return fetch("/getDoublePointsJoker", {
        method:"GET",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include"
    })
        .then(response => response.json())
        .catch(error => console.error("Fehler beim Abrufen der verfügbaren DoublePointsJoker:", error));
}

function setDoublePointsJoker(newJokers) {
    return fetch("/setDoublePointsJoker", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({newAmountOfJokers: newJokers})
    })
        .then(response => response.json())
        .catch(error => console.error("Fehler beim Setzen der neuen Anzahl DoublePointsJoker:", error));
}

function getCoins() {
    return fetch("/getCoins", {
        method: "GET",
        headers: {
            "Content-Type": "text/json"
        },
        credentials: "include"
    }).then(response => response.json())
        .catch(error => console.error("Fehler beim Abrufen der verfügbaren Coins", error));
}

function setCoins(newCoins) {
    fetch("/setCoins", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({newCoins: newCoins})
    }).then(document.getElementById("stock-text").innerText = newCoins)
        .catch(error => console.error("Fehler beim Setzen der neuen Coins:", error));
}