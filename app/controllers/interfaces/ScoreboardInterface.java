package controllers.interfaces;

import models.HighscoreFactory.Highscore;
import java.util.List;

public interface ScoreboardInterface {
    List<Highscore> getHighscoresOfQuiz(int quizId);
    List<Highscore> getHighscoresOfUser(int userId);
}
