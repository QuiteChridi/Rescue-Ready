function selectHighscore(quizId) {
    fetch("/highscoreByQuizName", {
        method: "POST",
        body: JSON.stringify({
            quizId: quizId,
        }),
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
    }).then(response => {
        window.location.href= response.url
    })
        .catch(error => console.error(`Fehler bei der Serveranfrage:`, error));
}

function changeQuizName(quizName) {
    localStorage.setItem('selectedQuizName', quizName);
    updateTitle();
}

function updateTitle() {
    const quizName = localStorage.getItem('selectedQuizName');
    if (quizName) {
        document.querySelector("#highscoreTitle").textContent = "Highscores: " + quizName;
    }
}

document.addEventListener('DOMContentLoaded', updateTitle);


