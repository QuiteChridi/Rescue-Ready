function getProfilePic() {
    fetch("/getProfilePic", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include"
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById("profileImg").src = "/assets/" + data;
        })
        .catch(error => console.error("Fehler beim Abrufen der verfügbaren Coins", error));
}