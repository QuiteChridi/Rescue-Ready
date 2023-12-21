let score = 0;
let quizStarted = false;

function startQuiz() {
    getNextQuestion();
    document.getElementById('question-container').style.display = 'block';
    quizStarted = true;
}

function updateScore() {
    score += 1;
    document.getElementById('current-score').innerText = "Aktueller Punktestand: " + score;
}

function renderNextQuestion(question, answers, score) {
    document.getElementById('start-quiz-container').innerText = "";
    document.getElementById('current-score').innerText = "Aktueller Punktestand: " + score;
    document.getElementById('question').innerHTML = question;

    let answersContainer = document.getElementById('answer-form');
    answersContainer.innerHTML = "";

    answers.forEach(answer => {
        let label = document.createElement('label');
        label.innerHTML = `<input type="radio" name="answer" value="${answer}">${answer}`;
        answersContainer.appendChild(label);
        answersContainer.appendChild(document.createElement('br'));
    });

    document.getElementById('check-answer-button').style.display = 'block';
    document.getElementById('next-question-button').style.display = 'none';
    document.getElementById('end-quiz-container').style.display = 'none';
    document.getElementById('result').innerText = "";
}

function getNextQuestion() {
    fetch("/getNextQuestion", {
        method: "GET",
        headers: {
            "Content-Type": "text/json"
        },
        credentials: "include"
    }).then(response => {
        if (response.status === 404) {
            document.getElementById('end-quiz-container').style.display = 'block';
            document.getElementById('next-question-button').style.display = 'none';
            return;
        }

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    }).then(data => {
        renderNextQuestion(data.question, data.answers, score);
    }).catch(error => console.log(error.message));
}

///// nur zum Testen genutzt
function displayConsoleMessage(message) {
    let consoleOutput = document.getElementById('console');
    consoleOutput.innerHTML += message + '<br>';
}
////

function checkAnswer() {
    let selectedAnswerElement = document.querySelector('input[name="answer"]:checked');

    if (selectedAnswerElement) {
        submitAnswer(selectedAnswerElement)
    } else {
        document.getElementById('result').textContent = 'Bitte wähle erst eine Antwort aus.'
    }
}

function submitAnswer(selectedAnswerElement) {
    const selectedAnswer = selectedAnswerElement ? selectedAnswerElement.value : null;

    fetch("/checkAnswer", {
        method: "POST",
        body: JSON.stringify({
            selectedAnswer: selectedAnswer,
        }),
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include"
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    }).then(data => {
        renderResult(data.isCorrect, data.correctAnswer);
        document.getElementById('check-answer-button').style.display = 'none';
        document.getElementById('next-question-button').style.display = 'block';
    }).catch(error => console.log(error.message));
}


function renderResult(isCorrect, correctAnswer) {
    let consoleOutput = document.getElementById('result');
    let message;

    if (isCorrect) {
        updateScore(score);
        message = "Richtige Antwort!";
    } else {
        message = "Falsch. Die richtige Antwort lautet: " + correctAnswer;
    }

    consoleOutput.innerHTML = message;
}



function safeQuizResult() {
    console.log("safeQuizResult wurde aufgerufen mit dem Highscore: ", highscore);

    fetch("/saveResult", {
        method: "POST",
        body: JSON.stringify({
            highscore: score
        }),
        headers: {
            "Content-Type": "application/json"
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
