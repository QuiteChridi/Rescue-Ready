package models;

import controllers.interfaces.ScoreboardInterface;
import oldModels.DummyScoreboard;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.*;

public class DummyScoreboardTest {
    private ScoreboardInterface scoreboard;

    @Before
    public void setUp(){
        scoreboard = DummyScoreboard.getInstance();
    }

    @Test
    public void getHighscoresShouldReturnACopyOfHighscores(){
        var highscores = scoreboard.getHighscores();
        highscores.add(new HighscoreFactory.Highscore("test", 123));
        assertNotEquals(highscores, scoreboard.getHighscores());
    }
    @Test
    public void updateHighscoreShouldUpdateHighscoreInList() {
        var highscores = scoreboard.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        HighscoreFactory.Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        int scoreToSet = usedHighscore.getScore() + 12;
        scoreboard.updateHighscore(usedHighscore.getUserName(), scoreToSet);
        assertEquals(scoreToSet, scoreboard.getHighscore(usedHighscore.getUserName()).getScore());
    }
    @Test
    public void updateHighscoreShouldThrowIfHighscoreIsNotInList() {
        assertThrows(IllegalArgumentException.class, () -> scoreboard.updateHighscore("", 20));
    }

    @Test
    public void isInHighscoreListShouldReturnTrueForHighscoreInList() {
        var highscores = scoreboard.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        HighscoreFactory.Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        assertTrue(scoreboard.isInHighscoreList(usedHighscore.getUserName()));
    }

    @Test
    public void isInHighscoreListShouldReturnFalseForHighscoreNotInList() {
        assertFalse(scoreboard.isInHighscoreList(""));
    }

    @Test
    public void getHighscoreShouldReturnTheHighscoreWithThePassedName(){
        var highscores = scoreboard.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        HighscoreFactory.Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        assertEquals(usedHighscore, scoreboard.getHighscore(usedHighscore.getUserName()));
    }

    @Test
    public void getHighscoreShouldReturnNullIfNameNotFound(){
        assertNull(scoreboard.getHighscore(""));
    }
    @Test
    public void highscoreListShouldContainHighscoreAfterAdding() {
        scoreboard.addHighscore("test", 123);
        assertTrue(scoreboard.getHighscores().contains(new HighscoreFactory.Highscore("test", 123)));
    }

    @Test
    public void getRankShouldReturnPositionInSortedHighscoreListPlusOne() {
        var highscores = scoreboard.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        HighscoreFactory.Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        highscores.sort(Comparator.comparingInt(HighscoreFactory.Highscore::getScore));
        Collections.reverse(highscores);
        assertEquals(scoreboard.getRank(usedHighscore.getUserName()), highscores.indexOf(usedHighscore) + 1 );

    }
}