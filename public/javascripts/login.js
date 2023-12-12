function validateLogin() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    fetch("/login", {
        method: 'POST',
        body: JSON.stringify({
            username: username,
            password: password
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("Wrong username or password")
            }
        })
        .then(data => {
            window.location.href = "/main";
        })
        .catch(error => {
            alert(error.message);
        })
}

function logout() {
    fetch("/logout", {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                window.location.href = "/login";
            } else {
                console.error("Logout failed");
            }
        })
        .catch(error => {
            console.error("An Error appeared: ", error);
        })
}