let correctAnswerCount = 0;
let time = 0;
let timerRunning = false;
let timerInterval;

function startQuiz() {
    getNextQuestion();
    startTimer();
}

function startTimer() {
    if (!timerRunning) {
        timerRunning = true;
        timerInterval = setInterval(function () {
            time++;
            updateTimerDisplay();
        }, 1000);
    }
    document.getElementById('timer').style.display = 'block';
}

function updateScore() {
    correctAnswerCount += 1;
    document.getElementById('current-score').innerText = "Richtige Antworten : " + correctAnswerCount;
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
        renderNextQuestion(data.question, data.answers, correctAnswerCount);
    }).catch(error => console.log(error.message));
}

function renderNextQuestion(question, answers, score) {
    document.getElementById('start-quiz-container').innerText = "";
    document.getElementById('current-score').innerText = "Aktueller Punktestand: " + score;
    document.getElementById('question-container').style.display = 'block';
    document.getElementById('question').innerHTML = question;

    let answersContainer = document.getElementById('answer-form');
    answersContainer.innerHTML = "";

    answers.forEach(answer => {
        let label = document.createElement('label');
        label.innerHTML = `<input type="radio" name="answer" value="${answer}">${answer}`;
        answersContainer.appendChild(label);
        answersContainer.appendChild(document.createElement('br'));
    });
    document.getElementById('button-container').style.display = 'block';
    document.getElementById('check-answer-button').style.display = 'block';
    document.getElementById('next-question-button').style.display = 'none';
    document.getElementById('end-quiz-container').style.display = 'none';
    document.getElementById('result').innerText = "";
}

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
        updateScore(correctAnswerCount);
        message = "Richtige Antwort!";
    } else {
        message = "Falsch. Die richtige Antwort lautet: " + correctAnswer;
    }

    consoleOutput.innerHTML = message;
}

function updateTimerDisplay() {
    const timerElement = document.getElementById('timer');
    timerElement.innerText = formatTime(time);
}

function formatTime(seconds) {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds < 10 ? '0' : ''}${remainingSeconds}`;
}

function stopTimer() {
    if (timerRunning) {
        clearInterval(timerInterval);
        timerRunning = false;
    }
}

function calculateHighscore() {
    if (correctAnswerCount === 0 ){
        return 0;
    }else{
        return Math.round(10*(correctAnswerCount+(1/time)*100));
    }
}

function saveEndScore() {
    stopTimer();
    saveQuizResult(calculateHighscore())
}

function saveQuizResult(score) {

    fetch("/saveQuizResult", {
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



///// nur zum Testen genutzt
function displayConsoleMessage(message) {
    let consoleOutput = document.getElementById('console');
    consoleOutput.innerHTML += message + '<br>';
}
////