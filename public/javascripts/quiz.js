let correctAnswerCount = 0;
let score = 0;
let questionTimer = 20;
let timerRunning = false;
let timerInterval;
let doubleIt = false;
let quizName = "";

function selectQuizAndGetName(quizId){
    fetch("/selectQuizAndGetName", {
        method: "POST",
        body: JSON.stringify({
            quizId: quizId,
        }),
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include"
    })
        .then(response => response.json())
        .then(data => {
            quizName = data.quizName;
            document.getElementById("quizName").innerHTML = "<h4 style='line-height: 2;'>Du hast dich für das Quiz zum Thema<br><u>" + quizName + "</u><br>entschieden</h4>";
            document.getElementById("welcome-container").style.display = "none"
            document.getElementById("start-quiz-container").style.display = "flex"
        })
        .catch(error =>
            console.log(error.message));
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
            document.getElementById('end-quiz-container').style.display = 'flex';
            document.getElementById('next-question-button').style.display = 'none';
        } else {
            startTimer();
            renderNextQuestion(data.question, data.answers, correctAnswerCount);
        }
    }).catch(error => console.log(error.message));
}

function startTimer() {
    if (!timerRunning) {
        questionTimer = 21;
        timerRunning = true;
        timerInterval = setInterval(function () {
            updateTimerDisplay();
            questionTimer--;
            updateTimerDisplay();

            if (questionTimer <= 0) {
                submitAnswer(null);
            }
        }, 1000);
    }
}

function updateTimerDisplay() {
    const timerBar = document.getElementById('timer-bar');
    const timerText = document.getElementById('timer-text');
    const container = document.getElementById('timer-bar-container');

    const percentage = (questionTimer / 20) * 100;

    const containerWidth = container.clientWidth * 0.91;
    const barWidth = Math.min((percentage / 100) * containerWidth, containerWidth);

    timerBar.style.width = barWidth + 'px';

    if (questionTimer <= 5) {
        const red = 255; // Farbe am Ende
        const green = Math.floor((questionTimer / 5) * 128);
        timerBar.style.backgroundColor = `rgb(${red},${green},0)`;
    } else {
        const red = Math.floor(((20 - questionTimer) / 15) * 255);
        const green = 128; // Farbe am Anfang
        timerBar.style.backgroundColor = `rgb(${red},${green},0)`;
    }

    timerText.innerText = formatTime(questionTimer);
    if (questionTimer <= 0) {
        timerBar.style.width = containerWidth + 'px';
    }
}


function renderNextQuestion(question, answers, score) {
    document.getElementById('start-quiz-container').style.display = "none";
    document.getElementById("quizStarted-container").style.display = "flex";

    document.getElementById("quizName-h1").innerHTML = "<h5><u> Quiz: " + quizName + "</u></h5>";

    document.getElementById('current-score').innerText = "Aktueller Punktestand: " + score;

    document.getElementById('question-container').style.display = 'flex';
    document.getElementById('question').innerHTML = question;
    enableRadioButtons();
    let answersContainer = document.getElementById('answer-form');
    answersContainer.innerHTML = "";
    shuffleAnswers(answers);

    answers.forEach(answer => {
        let label = document.createElement('label');
        label.style.display = 'flex';
        label.style.alignItems = 'center';

        label.innerHTML = `<input type="radio" name="answer" value="${answer}"><h5 style="margin-left: 5px;">${answer}</h5>`;

        label.style.marginBottom = "15px";
        answersContainer.appendChild(label);
    });


    document.getElementById('joker-container').style.display = 'flex';
    document.getElementById('jokerPicPath1').src = "/assets/images/fiftyFiftyJoker.png";
    document.getElementById('jokerPicPath2').src = "/assets/images/pauseJoker.png";
    document.getElementById('jokerPicPath3').src = "/assets/images/doubleItJoker.png";
    document.getElementById('jokerPicPath1').disabled = false;
    document.getElementById('jokerPicPath2').disabled = false;
    document.getElementById('jokerPicPath3').disabled = false;

    document.getElementById('check-answer-button').style.display = 'flex';
    document.getElementById('next-question-button').style.display = 'none';
    document.getElementById('end-quiz-container').style.display = 'none';

    document.getElementById('result').style.display = "none";
}

function backToSelection() {
    document.getElementById("start-quiz-container").style.display = "none"
    document.getElementById("welcome-container").style.display = "flex"
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
        if (data.isCorrect === true) {
            calculateHighscore();
        }
        stopTimer();
        renderResult(data.isCorrect, data.correctAnswer);
        disableRadioButtons();
        document.getElementById('jokerPicPath1').disabled = true;
        document.getElementById('jokerPicPath2').disabled = true;
        document.getElementById('jokerPicPath3').disabled = true;
        document.getElementById('check-answer-button').style.display = 'none';
        document.getElementById('next-question-button').style.display = 'flex';
    }).catch(error => console.log(error.message));
}

function renderResult(isCorrect, correctAnswer) {
    let message;
    let resultElement = document.getElementById("result");

    if (isCorrect) {
        updateScore(correctAnswerCount);
        message = "Richtige Antwort!";
        resultElement.classList.remove("incorrect");
        resultElement.classList.add("correct");
    } else {
        message = "Falsch. Die richtige Antwort lautet: " + correctAnswer;
        resultElement.classList.remove("correct");
        resultElement.classList.add("incorrect");
    }

    resultElement.style.display = "flex";
    resultElement.innerHTML = "<i>" + message + "</i>";
}

function disableRadioButtons() {
    let radioButtons = document.querySelectorAll('#answer-form input[type="radio"]');
    radioButtons.forEach(function(radioButton) {
        radioButton.disabled = true;
    });
}

function enableRadioButtons() {
    let radioButtons = document.querySelectorAll('#answer-form input[type="radio"]');
    radioButtons.forEach(function(radioButton) {
        radioButton.disabled = false;
    });
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
    if (doubleIt === true) {
        score += (2*questionTimer);
    } else {
        score += questionTimer;
    }
}

function handleEndOfQuiz(){
    saveQuizResult();
    setNewAmountOfCoins();
}

function setNewAmountOfCoins() {
    let coinsGained = Math.floor(score/10);
    getCoins().then(data => {
        let newAmountOfCoins = coinsGained + data.availableCoins
        setCoins(newAmountOfCoins);
    });

}

function saveQuizResult() {
    fetch("/saveQuizResult", {
        method: "POST",
        body: JSON.stringify({
            score: score
        }),
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include"
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        document.getElementById("quizStarted-container").style.display = "none"
        document.getElementById("welcome-container").style.display = "flex"
        lastScore(score);
    })
        .catch(error => console.error('Fehler beim Speichern des Ergebnisses:', error));
}

function lastScore(lastscore) {
    document.getElementById("selectQuiz-h3").innerHTML = "In deinem letzten Quiz hast du einen Score von " + lastscore + " erreicht!\nDas Ergebnis wurde gespeichert!";
    document.getElementById("selectQuiz-h3").style.color = "green";
    score = 0;
    correctAnswerCount = 0;
}

function getCorrectAnswer() {
    return fetch("/getCorrectAnswer", {
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
    }).catch(error => console.log(error.message));
}

function shuffleAnswers(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
}

function useJokerOfId(jokerID) {
    if (jokerID === 1) {
        useFiftyFiftyJoker();
    } else if (jokerID === 2) {
        usePauseJoker();
    } else if (jokerID === 3) {
        useDoublePointsJoker();
    } else {
        console.log("Joker nicht implementiert!!!")
    }
}

function useFiftyFiftyJoker() {
    if(!document.getElementById("jokerPicPath1").disabled) {
        useJoker(1).then(data => {
            if (data.success === true) {
                executeFiftyFiftyJoker();

                document.getElementById("jokerAmount1").innerText = data.newAmountOfJokers
                document.getElementById('jokerPicPath1').src = "/assets/images/fiftyFiftyNope.png";
                document.getElementById('jokerPicPath1').disabled = true;
            } else {
                console.log("Nicht genügend FiftyFiftyJoker verfügbar oder Joker wurde bereits genutzt");
            }
        })
    }
}

function executeFiftyFiftyJoker() {
    getCorrectAnswer().then(data => {
        let answerLabels = document.querySelectorAll('form#answer-form label');
        let correctAnswer = data.correctAnswer
        let correctAnswerLabel = Array.from(answerLabels).find(label => {
            let answer = label.innerText
            return answer.includes(correctAnswer);
        });

        let hiddenCount = 0;
        while (hiddenCount < 2) {
            let randomIndex = Math.floor(Math.random() * answerLabels.length);
            let label = answerLabels[randomIndex];
            if (label.style.display !== 'none' && label !== correctAnswerLabel) {
                label.style.display = 'none';
                hiddenCount++;
            }
        }
    })
}

function usePauseJoker() {
    if(!document.getElementById('jokerPicPath2').disabled){
        useJoker(2).then(data => {
            if (data.success === true) {
                executePauseJoker();

                document.getElementById("jokerAmount2").innerText = data.newAmountOfJokers;
                document.getElementById('jokerPicPath2').src = "/assets/images/pauseNope.png";
                document.getElementById('jokerPicPath2').disabled = true;
            } else {
                console.log("Nicht genügend PauseJoker verfügbar oder Joker wurde bereits genutzt");
            }
        })
    }
}

function executePauseJoker() {
    if (timerRunning === true) {
        clearInterval(timerInterval);
        timerRunning = false;
    }
}

function useDoublePointsJoker() {
    if(!document.getElementById('jokerPicPath3').disabled){
        useJoker(3).then(data => {
            if (data.success === true) {
                executeDoublePointsJoker();

                document.getElementById("jokerAmount3").innerText = data.newAmountOfJokers;
                document.getElementById('jokerPicPath3').src = "/assets/images/doubleItNope.png";
                document.getElementById('jokerPicPath3').disabled = true;
            } else {
                console.log("Nicht genügend DoublePointsJoker verfügbar oder Joker wurde bereits genutzt");
            }
        })
    }
}

function executeDoublePointsJoker() {
    doubleIt = true;
}

function useJoker(id) {
    return fetch("/useJoker", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({
            jokerId: id
        })
    }).then(response => response.json())
}