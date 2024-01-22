function selectQuiz(quizId){
    fetch("/selectQuiz", {
        method: "POST",
        body: JSON.stringify({
            quizId: quizId,
        }),
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include"
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        window.location.href="quizView"
    }).catch(error =>
        console.log(error.message));
}