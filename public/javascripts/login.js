function checkLogin() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    /*if (username === "admin" && password === "admin") {
        // Weiterleitung zur Main Page
        window.location.href = "main";
    } else {
        alert("Falscher Benutzername oder Passwort. Bitte versuchen Sie es erneut.");
    }*/

    fetch("/login", {
        method: "POST",
        body: JSON.stringify({
            data: "hello",
        }),
        headers: {
            "Content-Type": "text/json"
        },
        credentials: "include"})
        .then(result => result.json())
        .then(data => console.log(data));
}

