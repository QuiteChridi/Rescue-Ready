function selectHighscore(quizName) {
    fetch("/highscoreByQuizName", {
        method: "POST",
        body: JSON.stringify({
            quizName: quizName,
        }),
        headers: {
            "Content-Type": "application/json",
        },
        credentials: "include",
    }).then(response => {
        window.location.href="highscore"
    })
        .catch(error => console.error(`Fehler bei der Serveranfrage:`, error));
}

