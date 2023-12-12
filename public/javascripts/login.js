function checkLogin() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    fetch("/login", {
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
            window.location.href = "main"
        } else {
            alert(data.response)
        }
    })
    .catch(error => console.log(error.message))
}