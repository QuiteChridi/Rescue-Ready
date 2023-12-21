document.addEventListener('DOMContentLoaded', (event) => {
    const answerOptions = document.querySelectorAll('.answer-option');
    answerOptions.forEach(option => {
        option.addEventListener('click', function() {
            alert('Sie haben gewählt: ' + this.textContent);
        });
    });
});



    function calculateTotalscore(highscore){
        // Alten TotalScore fetchen
        // Alten TotalScore mit neuem Addieren
        // Alten score überschreiben
}