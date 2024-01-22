package controllers.interfaces;

import models.HighscoreFactory;

import java.util.List;

public interface ScoreboardInterface {

    List<HighscoreFactory.Highscore> getHighscoresOfQuiz(int quizId);
}
