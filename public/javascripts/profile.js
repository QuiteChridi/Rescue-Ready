document.addEventListener("DOMContentLoaded", function() {

    const profileDisplay = document.querySelector(".profile-display");
    const profileEditor = document.querySelector(".profile-editor");
    const userNameDisplay = document.getElementById("user-name");
    const editButton = document.getElementById("edit-button");
    const nameInput = document.getElementById("name-input");
    const saveButton = document.getElementById("save-button");
    const fileInput = document.getElementById("file-input");
    const profilePictureEdit = document.getElementById("profile-picture-edit");
    const profilePictureDisplay = document.getElementById("profile-picture");

    let originalName = userNameDisplay.textContent;
    let originalImageUrl = profilePictureDisplay.src;

    document.getElementById("change-pic-button").addEventListener("click", () => {
        fileInput.click();
    });

    fileInput.addEventListener("change", () => {
        const selectedFile = fileInput.files[0];
        if (selectedFile) {
            profilePictureEdit.src = URL.createObjectURL(selectedFile);
        }
    });

    editButton.addEventListener("click", () => {
        profileDisplay.style.display = "none";
        profileEditor.style.display = "inline-block"
        nameInput.value = originalName;
    });

    saveButton.addEventListener("click", () => {

        const newName = nameInput.value;
        userNameDisplay.textContent = newName;
        originalName = newName;

        profilePictureDisplay.src = profilePictureEdit.src || originalImageUrl;
        originalImageUrl = profilePictureDisplay.src;

        profileDisplay.style.display = "inline-block";
        profileEditor.style.display = "none";
    });
});