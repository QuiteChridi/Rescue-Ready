package controllers.interfaces;

import java.util.List;

public interface AbstractHighscoreFactory {
    List<Highscore> getHighscoresOfQuiz(int quizId);

    List<Highscore> getHighscoresOfUser(int userId);
}
