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

function buyFiftyFiftyJoker() {
    getAvailableCoins()
        .then(data => {

            let availableCoins = data.availableCoins;
            console.log("Verfügbare Coins: " + availableCoins);

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
                        console.log("Neue Anzahl an Coins:", newCoins);
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
            console.log("Verfügbare Coins: " + availableCoins);

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
                        console.log("Neue Anzahl an Coins:", newCoins);
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
            console.log("Verfügbare Coins: " + availableCoins);

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
                        console.log("Neue Anzahl an Coins:", newCoins);
                    })
                    .catch(error => console.error("Fehler beim Setzen der neuen Coins:", error));
            } else {
                console.log("Nicht genügend Coins verfügbar.");
            }
        });
}