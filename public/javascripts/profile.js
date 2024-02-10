
function editProfile(){
    document.getElementById("profile-container").style.display = "none";
    document.getElementById("editProfile-container").style.display = "flex"
}

function saveChanges() {
    let username = document.getElementById("name-input").value;
    let password = document.getElementById("password-input").value;
    let email = document.getElementById("email-input").value;
    let profilePic = document.getElementById("newProfilePicture").files[0];
    let profilePicPath = ""
    if(profilePic != null){
        profilePicPath = profilePic.name;
    }

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
        } else {
            location.reload();
        }
    }).catch(error => console.log(error.message))
}
