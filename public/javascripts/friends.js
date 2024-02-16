const BASE_IMAGE_URL = "/assets/images/profilePics/";
const BASE_PROFILE_URL = "/friendProfile/";

/**
 * Add event listener to all add friend buttons
 */
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.add-friend-button').forEach(button => {
        button.addEventListener('click', function () {
            const userId = this.getAttribute('data-user-id');
            addFriend(userId);
        });
    });
});

/**
 * Add a friend to the friend list of the current user and reload the page to update the friend list
 * @param userId
 */
function addFriend(userId) {
    fetch(`/addFriend/${userId}`, {method: 'POST'})
        .then(response => {
            if (response.ok) {
                alert('Freund hinzugefügt');
                location.reload();
            } else {
                alert('Fehler beim Hinzufügen des Freundes');
            }
        })
        .catch(error => console.error('Fehler:', error));
}

/**
 * Remove a friend from the friend list of the current user and reload the page to update the friend list
 * @param friendId
 */
function removeFriend(friendId) {
    fetch(`/removeFriend/${friendId}`, {
        method: 'POST',
        credentials: 'include'
    }).then(response => {
        if (response.ok) {
            alert('Freund entfernt');
            location.reload();
        } else {
            alert('Fehler beim Entfernen des Freundes');
        }
    }).catch(error => console.error('Fehler:', error));
}

/**
 * Search for users by name and update the user list
 */

let originalUserList = [];

document.addEventListener('DOMContentLoaded', function () {
        const userListElements = document.querySelectorAll('.user-list li');
        originalUserList = Array.from(userListElements).map(li => {
                return {
                    id: li.querySelector('button').getAttribute('data-user-id'),
                    name: li.querySelector('a').textContent,
                    profilePicPath: li.querySelector('img').src
                };
            }
        );

    }
);

function filterAndHighlightUsers(query) {
    const userList = document.querySelector('.user-list');
    userList.innerHTML = '';

    const filteredUsers = originalUserList.filter(user => user.name.toLowerCase().includes(query.toLowerCase()));

    filteredUsers.forEach(user => {
        const li = document.createElement('li');
        li.classList.add('flex', 'flex-row', 'align-items-center', 'justify-content-center', 'gap-5', 'padding-5');
        li.innerHTML = `
            <img src="${user.profilePicPath}" alt="Profile Picture" style="width: 10%; max-height: 40px; max-width: 40px; border-radius: 50%;">
            <a href="${BASE_PROFILE_URL}${user.id}" style="width: 50%; font-size: 25px;">${user.name}</a>
            <button class="add-friend-button" data-user-id="${user.id}" style="width: 20%;">Hinzufügen</button>
        `;
        if (query && user.name.toLowerCase().includes(query.toLowerCase())) {
            li.classList.add('highlight');
        }
        userList.appendChild(li);
        addEventListenersToButtons();
    });
}

const searchInput = document.getElementById('newFriend');
if (searchInput) {
    searchInput.addEventListener('input', function () {
        filterAndHighlightUsers(this.value.trim());
    });
} else {
    console.error('Suchfeld mit ID "newFriend" wurde nicht gefunden.');
}


function addEventListenersToButtons() {
    document.querySelectorAll('.add-friend-button').forEach(button => {
        button.addEventListener('click', function() {
            const userId = this.getAttribute('data-user-id');
            addFriend(userId);
        });
    });
}
