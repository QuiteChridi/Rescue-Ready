package models;

import java.util.List;

public interface Database {
    List<Highscore> getHighscores();

    Highscore getHighscore(String name);

    int getRank(String name);

    void updateHighscore(String name, int score);

    void addHighscore(String name, int score);

    boolean isInList(String name);

    List<QuizQuestion> getQuizQuestions();
}
