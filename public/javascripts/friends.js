document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.add-friend-button').forEach(button => {
        button.addEventListener('click', function() {
            const userId = this.getAttribute('data-user-id');
            addFriend(userId);
        });
    });
});

function addFriend(userId) {
    fetch(`/addFriend/${userId}`, { method: 'POST' })
        .then(response => {
            if (response.ok) {
                alert('Freund hinzugefügt');
                location.reload(); // Um die Seite zu aktualisieren und die Freundesliste neu zu laden
            } else {
                alert('Fehler beim Hinzufügen des Freundes');
            }
        })
        .catch(error => console.error('Fehler:', error));
}
