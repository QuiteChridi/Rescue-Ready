function buyJoker(id) {
    return fetch("/buyJoker", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({
            jokerId: id
        })
    })
        .then(response => response.json())
        .then(data => {
            if(data.success === true){
                document.getElementById("stock-text").innerText = data.newAmountOfCoins
                document.getElementById(id).innerText = data.newAmountOfJokers;
            }else {
                alert("Nicht genügen Coins verfügbar");
            }
        })
        .catch(error => console.error("Fehler beim Setzen der neuen Anzahl Joker:", error));
}



