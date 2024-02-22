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

