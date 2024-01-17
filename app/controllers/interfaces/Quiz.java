package controllers.interfaces;

import models.QuizQuestion;

public interface Quiz {
    QuizQuestion getCurrentQuestion();

    void nextQuestion();

    boolean hasNextQuestion();

    boolean isCorrectAnswer(String answer);

    String getCorrectAnswer();

    void resetQuiz();

    void setStartingQuestion(int questionIndex);

}
