package controllers.interfaces;

import models.QuizFactory.QuizQuestion;

public interface QuizInterface {
    QuizQuestion getCurrentQuestion();

    void nextQuestion();

    boolean hasNextQuestion();

    boolean isCorrectAnswer(String answer);

    String getCorrectAnswer();

    int getId();

    String getName();
}
