// Elemente aus dem DOM auswählen
const profileDisplay = document.querySelector(".profile-display");
const profileEditor = document.querySelector(".profile-editor");
const userNameDisplay = document.getElementById("user-name");
const editButton = document.getElementById("edit-button");
const nameInput = document.getElementById("name-input");
const saveButton = document.getElementById("save-button");
const fileInput = document.getElementById("file-input");
const profilePictureEdit = document.getElementById("profile-picture-edit");
const profilePictureDisplay = document.getElementById("profile-picture");

// Ursprünglicher Name auf der Anzeigeseite
let originalName = userNameDisplay.textContent;

// Ursprüngliche Bild-URL auf der Anzeigeseite
let originalImageUrl = profilePictureDisplay.src;

// Event-Handler für den "Edit Picture" Button
document.getElementById("change-pic-button").addEventListener("click", () => {
    fileInput.click(); // Öffne den Datei-Explorer, wenn auf den Button geklickt wird

});

// Event-Handler für das Ändern des ausgewählten Bilds
fileInput.addEventListener("change", () => {
    const selectedFile = fileInput.files[0];
    if (selectedFile) {
        profilePictureEdit.src = URL.createObjectURL(selectedFile);
    }
});

// Event-Handler für den "Edit" Button
editButton.addEventListener("click", () => {

    profileDisplay.style.display = "none";
    profileEditor.style.display = "inline-block"

    // Setze den ursprünglichen Namen als Wert im Eingabefeld
    nameInput.value = originalName;
});

// Event-Handler für den "Save" Button
saveButton.addEventListener("click", () => {

    // Aktualisiere den Benutzernamen mit dem Wert aus dem Eingabefeld
    const newName = nameInput.value;
    userNameDisplay.textContent = newName;
    originalName = newName; // Aktualisiere den ursprünglichen Namen

    // Aktualisiere das Bild auf der Anzeigeseite mit dem ausgewählten Bild oder dem alten Bild
    profilePictureDisplay.src = profilePictureEdit.src || originalImageUrl;
    originalImageUrl = profilePictureDisplay.src; // Aktualisiere die ursprüngliche Bild-URL

    profileDisplay.style.display = "inline-block";
    profileEditor.style.display = "none";
});