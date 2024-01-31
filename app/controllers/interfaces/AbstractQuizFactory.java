package controllers.interfaces;

import models.QuizFactory;

import java.util.Map;

public interface AbstractQuizFactory {
    QuizFactory.Quiz getQuizById(int id);

    Map<Integer, String> getPossibleQuizNames();
}
