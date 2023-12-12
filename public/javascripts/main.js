document.addEventListener('DOMContentLoaded', (event) => {
    const answerOptions = document.querySelectorAll('.answer-option');
    answerOptions.forEach(option => {
        option.addEventListener('click', function() {
            alert('Sie haben gewählt: ' + this.textContent);
        });
    });
});