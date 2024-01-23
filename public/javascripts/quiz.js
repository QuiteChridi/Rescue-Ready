let correctAnswerCount = 0;
let time = 0;
let timerRunning = false;
let timerInterval;
let doubleIt = false;

function startQuiz() {
    getNextQuestion();
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
    if (doubleIt === true) {
        correctAnswerCount += 2;
        doubleIt = false;
    } else {
        correctAnswerCount += 1;
    }
    document.getElementById('current-score').innerText = "Aktueller Punktestand: " + correctAnswerCount;
}

function getNextQuestion() {
    fetch("/getNextQuestion", {
        method: "GET",
        headers: {
            "Content-Type": "text/json"
        },
        credentials: "include"
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    }).then(data => {
        if (data.endOfQuiz) {
            document.getElementById('end-quiz-container').style.display = 'block';
            document.getElementById('next-question-button').style.display = 'none';
        } else {
            renderNextQuestion(data.question, data.answers, correctAnswerCount);
            startTimer();
        }
    }).catch(error => console.log(error.message));
}

function renderNextQuestion(question, answers, score) {
    document.getElementById('start-quiz-container').innerText = "";
    document.getElementById('current-score').innerText = "Aktueller Punktestand: " + score;
    document.getElementById('questionCoandJoker').style.display = 'block';
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
    document.getElementById('joker-container').style.display = 'block';
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
        window.location.href="quizSelection"
        alert("Ergebnis gespeichert!");
    })
    .catch(error => console.error('Fehler beim Speichern des Ergebnisses:', error));
}

function getCorrectAnswer(callback) {
    fetch("/getCorrectAnswer", {
        method: "GET",
        headers: {
            "Content-Type": "text/json"
        },
        credentials: "include"
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    }).then(data => {
        callback(data.correctAnswer);
    }).catch(error => console.log(error.message));
}

function fiftyFiftyJoker() {
    getCorrectAnswer(correctAnswer => {
        let answerLabels = document.querySelectorAll('form#answer-form label');

        let correctAnswerLabel = Array.from(answerLabels).find(label => {
            return label.innerText.includes(correctAnswer);
        });

        let visibleAnswerCount = 0;
        answerLabels.forEach(label => {
            if (label.style.display !== 'none' && label !== correctAnswerLabel) {
                visibleAnswerCount++;
            }
        });

        if (visibleAnswerCount >= 2) {
            let hiddenCount = 0;
            while (hiddenCount < 2) {
                let randomIndex = Math.floor(Math.random() * answerLabels.length);
                let label = answerLabels[randomIndex];
                if (label.style.display !== 'none' && label !== correctAnswerLabel) {
                    label.style.display = 'none';
                    hiddenCount++;
                }
            }
        }

        document.getElementById('5050Joker').disabled = true;
    });
}

function pauseJoker() {
    clearInterval(timerInterval);
    timerRunning = false;
    document.getElementById('pauseJoker').disabled = true;
}

function doublePointsJoker() {
    doubleIt = true;
    document.getElementById('doublePointsJoker').disabled = true;
}






