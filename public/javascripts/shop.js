const FIFTY_FIFTY_PRICE = 2;
const PAUSE_PRICE = 3;
const DOUBLE_POINTS_PRICE = 5;


function buyJoker(id) {
    getCoins()
        .then(data =>{
            let availableCoins = data.availableCoins

            if(availableCoins >= FIFTY_FIFTY_PRICE){
                availableCoins = availableCoins - FIFTY_FIFTY_PRICE;
                setCoins(availableCoins)

                let availableJokers = Number(document.getElementById("fiftyFiftyJokerAmount").innerText) + 1;

                setJoker(availableJokers, id).then(data => {
                    document.getElementById("fiftyFiftyJokerAmount").innerText = data.newAmountOfJokers;
                });
            } else {
                alert("Nicht genügen Coins verfügbar");
            }
        });
}

function buyFiftyFiftyJoker() {
    getCoins()
        .then(data =>{
        let availableCoins = data.availableCoins

        if(availableCoins >= FIFTY_FIFTY_PRICE){
            availableCoins = availableCoins - FIFTY_FIFTY_PRICE;
            setCoins(availableCoins)

            let availableJokers = Number(document.getElementById("fiftyFiftyJokerAmount").innerText) + 1;

            setFiftyFiftyJoker(availableJokers).then(data => {
                document.getElementById("fiftyFiftyJokerAmount").innerText = data.newAmountOfJokers;
            });
        } else {
            alert("Nicht genügen Coins verfügbar");
        }
    });
}

function buyPauseJoker() {
    getCoins()
        .then(data =>{
            let availableCoins = data.availableCoins

            if(availableCoins >= PAUSE_PRICE){
                availableCoins = availableCoins - PAUSE_PRICE;
                setCoins(availableCoins)

                let availableJokers = Number(document.getElementById("pauseJokerAmount").innerText) + 1;

                setPauseJoker(availableJokers).then(data => {
                    document.getElementById("pauseJokerAmount").innerText = data.newAmountOfJokers;
                });
            } else {
                alert("Nicht genügen Coins verfügbar");
            }
        });
}

function buyDoublePointsJoker() {
    getCoins()
        .then(data =>{
            let availableCoins = data.availableCoins

            if(availableCoins >= DOUBLE_POINTS_PRICE){
                availableCoins = availableCoins - DOUBLE_POINTS_PRICE;
                setCoins(availableCoins)

                let availableJokers = Number(document.getElementById("doublePointsJokerAmount").innerText) + 1;

                setPauseJoker(availableJokers).then(data => {
                    document.getElementById("doublePointsJokerAmount").innerText = data.newAmountOfJokers;
                });
            } else {
                alert("Nicht genügen Coins verfügbar");
            }
        });
}



