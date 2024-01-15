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
    })
        .then(response => response.json())
        .then(data => {
            HomeController.renderHighscore(data);
        })
        .catch(error => console.error("Error:", error));
}
