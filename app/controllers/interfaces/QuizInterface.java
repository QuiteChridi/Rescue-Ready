package controllers.interfaces;

import models.QuizFactory;

public interface QuizInterface {
    QuizFactory.QuizQuestion getCurrentQuestion();

    void nextQuestion();

    boolean hasNextQuestion();

    boolean isCorrectAnswer(String answer);

    String getCorrectAnswer();

    int getId();

    String getName();
}
