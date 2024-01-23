package controllers.interfaces;

import models.HighscoreFactory;

import java.util.List;

public interface ScoreboardInterface {
    List<HighscoreFactory.Highscore> getHighscoresOfQuiz(int quizId);
    List<HighscoreFactory.Highscore> getHighscoresOfUser(int userId);
}
