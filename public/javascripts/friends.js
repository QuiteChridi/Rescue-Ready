const BASE_IMAGE_URL = "/assets/images/profilePics/profilePic";
const BASE_PROFILE_URL = "/friendProfile/";

let originalUserList = [];

document.addEventListener('DOMContentLoaded', function () {
    const userListElements = document.querySelectorAll('.user-list li');
    originalUserList = Array.from(userListElements).map(li => {
        return {
            id: li.querySelector('button').getAttribute('data-user-id'),
            name: li.querySelector('a').textContent,
            profilePicPath: li.querySelector('img').src.replace(location.origin, '')
        };
    });

    document.querySelectorAll('.add-friend-button').forEach(button => {
        button.addEventListener('click', function () {
            const userId = this.getAttribute('data-user-id');
            const user = originalUserList.find(u => u.id === userId);
            const userName = user ? user.name : 'Der Benutzer';
            addFriend(userId, userName);
        });
    });

    const searchInput = document.getElementById('newFriend');
    if (searchInput) {
        searchInput.addEventListener('input', function () {
            filterAndHighlightUsers(this.value.trim());
        });
    } else {
        console.error('Suchfeld mit ID "newFriend" wurde nicht gefunden.');
    }

    document.querySelectorAll('.chat-button').forEach(button => {
        button.addEventListener('click', function() {
            const receiverId = this.getAttribute('data-receiver-id');
            openChat(receiverId);
        });
    });

    document.getElementById('chat-send').addEventListener('click', function(event) {
        event.preventDefault();
        const chatContainer = document.getElementById('chat-container');
        const receiverId = chatContainer.getAttribute('data-receiver-id');
        const messageText = document.getElementById('chat-input').value;

        sendMessage(receiverId, messageText);
    });
});

function addFriend(userId, userName) {
    fetch(`/addFriend/${userId}`, {method: 'POST'})
        .then(response => {
            if (response.ok) {
                alert(`${userName} wurde erfolgreich hinzugefügt.`);
                location.reload();
            } else {
                alert(`${userName} existiert bereits in deiner Freundesliste.`);
            }
        })
        .catch(error => console.error('Fehler:', error));
}

function removeFriend(friendId, userName) {
    fetch(`/removeFriend/${friendId}`, {method: 'POST', credentials: 'include'})
        .then(response => {
            if (response.ok) {
                alert(`Der Nutzer wurde erfolgreich entfernt.`);
                location.reload();
            } else {
                alert(`Fehler beim Entfernen von ${userName}.`);
            }
        })
        .catch(error => console.error('Fehler:', error));
}

function filterAndHighlightUsers(query) {
    const userList = document.querySelector('.user-list');
    userList.innerHTML = '';

    const filteredUsers = originalUserList.filter(user => user.name.toLowerCase().includes(query.toLowerCase()));

    filteredUsers.forEach(user => {
        const li = document.createElement('li');
        li.classList.add('flex', 'flex-row', 'align-items-center', 'justify-content-center', 'gap-5', 'padding-5');
        li.innerHTML = `
            <img src="${user.profilePicPath}" alt="Profile Picture" style="width: 10%; max-height: 40px; max-width: 40px; border-radius: 50%;">
            <a href="${BASE_PROFILE_URL}${user.id}" style="width: 70%; font-size: 25px;">${user.name}</a>
            <button class="add-friend-button" data-user-id="${user.id}" style="width: 20%;">Hinzufügen</button>
        `;
        if (query && user.name.toLowerCase().includes(query.toLowerCase())) {
            li.classList.add('highlight');
        }
        userList.appendChild(li);
        addEventListenersToButtons();
    });
}



function addEventListenersToButtons() {
    document.querySelectorAll('.add-friend-button').forEach(button => {
        button.addEventListener('click', function() {
            const userId = this.getAttribute('data-user-id');
            addFriend(userId);
        });
    });
}

function openChat(receiverId) {
    // Stellen Sie sicher, dass der Chat-Container sichtbar ist
    const chatContainer = document.getElementById('chat-container');
    chatContainer.style.display = 'block';

    // Setzen oder aktualisieren Sie die receiverId für den Chat-Container
    chatContainer.setAttribute('data-receiver-id', receiverId);

    // Löschen Sie alle vorhandenen Nachrichten im Chat-Container
    const messagesContainer = document.getElementById('chat-messages');
    messagesContainer.innerHTML = '';

    // Laden Sie die Nachrichten für diesen spezifischen Freund
    fetchMessages(receiverId);
}


function sendMessage(receiverId, messageText) {
    if (!messageText.trim()) {
        return;
    }
    fetch(`/sendMessage/${receiverId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ content: messageText })
    })
        .then(response => {
            if (response.ok) {
                document.getElementById('chat-input').value = '';
                appendMessageToChat(messageText, 'sent');
                fetchMessages(receiverId);

            } else {
                alert('Fehler beim Senden der Nachricht.');
            }
        })
        .catch(error => {
            console.error('Fehler beim Senden der Nachricht:', error);
        });
}

function fetchMessages(receiverId) {
    fetch(`/getMessages/${receiverId}`)
        .then(response => response.json())
        .then(messages => {
            const messagesContainer = document.getElementById('chat-messages');
            messagesContainer.innerHTML = '';
            messages.forEach(message => {
                appendMessageToChat(message.content, message.direction);
            });
        })
        .catch(error => {
            console.error('Fehler beim Abrufen der Nachrichten:', error);
        });
}

function appendMessageToChat(messageText, direction) {
    const messagesContainer = document.getElementById('chat-messages');
    const messageElement = document.createElement('div');
    messageElement.classList.add('chat-messages', direction); //messages
    messageElement.textContent = messageText;
    messagesContainer.appendChild(messageElement);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}
