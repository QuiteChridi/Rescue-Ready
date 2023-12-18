document.addEventListener('DOMContentLoaded', (event) => {
    const answerOptions = document.querySelectorAll('.answer-option');
    answerOptions.forEach(option => {
        option.addEventListener('click', function() {
            alert('Sie haben gewählt: ' + this.textContent);
        });
    });
});

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

    function calculateTotalscore(highscore){
        // Alten TotalScore fetchen
        // Alten TotalScore mit neuem Addieren
        // Alten score überschreiben
}