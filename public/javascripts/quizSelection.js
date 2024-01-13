function selectQuiz(id){
    fetch("/selectQuiz", {
        method: "POST",
        body: JSON.stringify({
            quizName: id,
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