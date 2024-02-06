function checkLogin() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    fetch("/authenticate", {
        method: "POST",
        body: JSON.stringify({

            username: username,
            password: password

        }),
        headers: {
            "Content-Type": "text/json"
        },
        credentials: "include"
    }).then(response  => {
        if (!response.ok){
            throw new Error('HTTP error! Status: ${result.status}')
        }
        return response.json()
    }).then(data => {
        if(data.response === "Login successful"){
            window.location.href = "quizSelection"
        } else {
            alert(data.response)
        }
    }).catch(error => console.log(error.message))
}


function logout() {
    fetch("/logout", {
        method: 'POST',
        credentials: 'include'
    }).then(response => {
            if (response.ok) {
                window.location.href = "/";
            } else {
                console.error("Logout failed");
            }
        })
        .catch(error => {
            console.error("An Error appeared: ", error);
        })
}

function signup() {
    let username = document.getElementById("newUsername").value;
    let password = document.getElementById("newPassword").value;
    let email = document.getElementById("email").value;

    fetch("/signup", {
        method: "POST",
        body: JSON.stringify({

            username: username,
            password: password,
            email: email

        }),
        headers: {
            "Content-Type": "text/json"
        },
        credentials: "include"
    }).then(response  => {
        if (!response.ok){
            throw new Error('HTTP error! Status: ${result.status}')
        }
        return response.json()
    }).then(data => {
        if(data.response === "Signup successful"){
            window.location.href = "quizSelection"
        } else {
            alert(data.response)
        }
    }).catch(error => console.log(error.message))
}