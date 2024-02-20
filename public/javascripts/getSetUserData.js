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
    }).catch(error => console.error("Fehler beim Setzen der neuen Coins:", error));
}