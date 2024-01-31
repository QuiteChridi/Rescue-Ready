package controllers.interfaces;

import models.HighscoreFactory.Highscore;
import java.util.List;

public interface AbstractHighscoreFactory {
    List<Highscore> getHighscoresOfQuiz(int quizId);

    List<Highscore> getHighscoresOfUser(int userId);
}
