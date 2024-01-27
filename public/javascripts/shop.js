function getAvailableCoins() {
    return fetch("/getAvailableCoins", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include"
    })
        .then(response => response.json())
        .catch(error => console.error("Fehler beim Abrufen der verfügbaren Coins", error));
}

function updateCoinsDisplay(newCoins) {
    document.getElementById("stock-text").innerText = newCoins;
}

function buyFiftyFiftyJoker() {
    getAvailableCoins()
        .then(data => {

            let availableCoins = data.availableCoins;
            let fiftyfiftyPrice = 2;

            if (availableCoins >= fiftyfiftyPrice) {
                fetch("/setNewCoins", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: "include",
                    body: JSON.stringify({ newCoins: availableCoins - fiftyfiftyPrice })
                })
                    .then(response => response.json())
                    .then(data => {
                        let newCoins = data.newCoins;
                        updateCoinsDisplay(newCoins);

                        let availableFiftyFiftyJoker =  document.getElementById("fiftyFiftyJokerAmount").innerText;
                        availableFiftyFiftyJoker++;

                        fetch("/setFiftyFiftyJoker", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            credentials: "include",
                            body: JSON.stringify({ newAmountOfJokers: availableFiftyFiftyJoker})
                        })
                            .then(response => response.json())
                            .then(data => {
                                document.getElementById("fiftyFiftyJokerAmount").innerText = data.newAmountOfJokers;
                            })
                            .catch(error => console.error("Fehler beim Setzen der neuen Anzahl FiftyFiftyJoker:", error));
                    })
                    .catch(error => console.error("Fehler beim Setzen der neuen Coins:", error));
            } else {
                console.log("Nicht genügend Coins verfügbar.");
            }
        });
}

function buyPauseJoker() {
    getAvailableCoins()
        .then(data => {

            let availableCoins = data.availableCoins;
            let pausePrice = 3;

            if (availableCoins >= pausePrice) {
                fetch("/setNewCoins", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: "include",
                    body: JSON.stringify({ newCoins: availableCoins - pausePrice })
                })
                    .then(response => response.json())
                    .then(data => {

                        let newCoins = data.newCoins;
                        updateCoinsDisplay(newCoins);

                        let availablePauseJoker =  document.getElementById("pauseJokerAmount").innerText;
                        availablePauseJoker++;

                        fetch("/setPauseJoker", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            credentials: "include",
                            body: JSON.stringify({ newAmountOfJokers: availablePauseJoker})
                        })
                            .then(response => response.json())
                            .then(data => {
                                document.getElementById("pauseJokerAmount").innerText = data.newAmountOfJokers;
                            })
                            .catch(error => console.error("Fehler beim Setzen der neuen Anzahl PauseJoker:", error));
                    })
                    .catch(error => console.error("Fehler beim Setzen der neuen Coins:", error));
            } else {
                console.log("Nicht genügend Coins verfügbar.");
            }
        });
}

function buyDoublePointsJoker() {
    getAvailableCoins()
        .then(data => {

            let availableCoins = data.availableCoins;
            let doublePointsPrice = 4;

            if (availableCoins >= doublePointsPrice) {
                fetch("/setNewCoins", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: "include",
                    body: JSON.stringify({ newCoins: availableCoins - doublePointsPrice })
                })
                    .then(response => response.json())
                    .then(data => {

                        let newCoins = data.newCoins;
                        updateCoinsDisplay(newCoins);

                        let availableDoublePointsJoker =  document.getElementById("doublePointsJokerAmount").innerText;
                        availableDoublePointsJoker++;

                        fetch("/setDoublePointsJoker", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            credentials: "include",
                            body: JSON.stringify({ newAmountOfJokers: availableDoublePointsJoker})
                        })
                            .then(response => response.json())
                            .then(data => {
                                console.log(data)
                                document.getElementById("doublePointsJokerAmount").innerText = data.newAmountOfJokers;
                            })
                            .catch(error => console.error("Fehler beim Setzen der neuen Anzahl DoublePointsJoker:", error));
                    })
                    .catch(error => console.error("Fehler beim Setzen der neuen Coins:", error));
            } else {
                console.log("Nicht genügend Coins verfügbar.");
            }
        });
}