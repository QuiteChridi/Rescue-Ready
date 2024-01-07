package controllers.interfaces;

import models.Highscore;

import java.util.List;

public interface Scoreboard {

    List<Highscore> getHighscores();

    Highscore getHighscore(String name);

    int getRank(String name);

    void updateHighscore(String name, int score);

    void addHighscore(String name, int score);

    boolean isInHighscoreList(String name);


}
