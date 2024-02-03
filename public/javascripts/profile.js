
function editProfile(){
    document.querySelector(".profile-display").style.display = "none";
    document.querySelector(".profile-editor").style.display = "inline-block"
}

function saveChanges(){
    saveChangesToUserData();
    document.getElementsByTagName("form").item(0).submit();
}

function saveChangesToUserData() {
    let username = document.getElementById("name-input").value;
    let password = document.getElementById("password-input").value;
    let email = document.getElementById("email-input").value;
    let profilePicPath = document.getElementById("profile-picture-edit");

    fetch("/saveUser", {
        method: "POST",
        body: JSON.stringify({
            username: username,
            email: email,
            password: password,
            profilePicPath: profilePicPath
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
        if(data.success !== true){
            alert("something went wrong")
        }
    }).catch(error => console.log(error.message))
}