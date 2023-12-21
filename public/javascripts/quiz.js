
function renderNextQuestion(question,answers){ //question: String, answers: String[]
    //todo render Next question
}

function getNextQuestion() {
    fetch("/getNextQuestion", {
        method: "GET",
        headers: {
            "Content-Type": "text/json"
        },
        credentials: "include"
    }).then(response  => {
        if (!response.ok){
            throw new Error('HTTP error! Status: ${result.status}')
        }
        return response.json()
    }).then(data => {
        renderNextQuestion(data.question, data.answers)
    }).catch(error => console.log(error.message))
}

function renderResult(score, rightAnswer){//score: int, rightAnswer: String
    getNextQuestion()
    //todo render correct/incorrect answer + score
}

function submitAnswer(){
    //todo post correct answer

    renderResult(score, rightAnswer)
}

function safeQuizResult(highscore) {
    console.log("safeQuizResult wurde aufgerufen mit dem Highscore: ", highscore);

    fetch("/saveResult", {
        method: "POST",
        body: JSON.stringify({
            highscore: highscore
        }),
        headers: {
            "Content-Type": "application/json" // Hier wird der Header gesetzt
        },
        credentials: "include"
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    }).then(data => {
        alert("Ergebnis gespeichert!");
    }).catch(error => console.error('Fehler beim Speichern des Ergebnisses:', error));
}