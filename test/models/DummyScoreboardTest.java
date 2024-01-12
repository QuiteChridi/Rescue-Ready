package models;

import controllers.interfaces.Scoreboard;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.*;

public class DummyScoreboardTest {
    private Scoreboard scoreboard;

    @Before
    public void setUp(){
        scoreboard = DummyScoreboard.getInstance();
    }

    @Test
    public void getHighscoresShouldReturnACopyOfHighscores(){
        var highscores = scoreboard.getHighscores();
        highscores.add(new Highscore("test", 123));
        assertNotEquals(highscores, scoreboard.getHighscores());
    }
    @Test
    public void updateHighscoreShouldUpdateHighscoreInList() {
        var highscores = scoreboard.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        int scoreToSet = usedHighscore.getScore() + 12;
        scoreboard.updateHighscore(usedHighscore.getName(), scoreToSet);
        assertEquals(scoreToSet, scoreboard.getHighscore(usedHighscore.getName()).getScore());
    }
    @Test
    public void updateHighscoreShouldThrowIfHighscoreIsNotInList() {
        assertThrows(IllegalArgumentException.class, () -> scoreboard.updateHighscore("", 20));
    }

    @Test
    public void isInHighscoreListShouldReturnTrueForHighscoreInList() {
        var highscores = scoreboard.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        assertTrue(scoreboard.isInHighscoreList(usedHighscore.getName()));
    }

    @Test
    public void isInHighscoreListShouldReturnFalseForHighscoreNotInList() {
        assertFalse(scoreboard.isInHighscoreList(""));
    }

    @Test
    public void getHighscoreShouldReturnTheHighscoreWithThePassedName(){
        var highscores = scoreboard.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        assertEquals(usedHighscore, scoreboard.getHighscore(usedHighscore.getName()));
    }

    @Test
    public void getHighscoreShouldReturnNullIfNameNotFound(){
        assertNull(scoreboard.getHighscore(""));
    }
    @Test
    public void highscoreListShouldContainHighscoreAfterAdding() {
        scoreboard.addHighscore("test", 123);
        assertTrue(scoreboard.getHighscores().contains(new Highscore("test", 123)));
    }

    @Test
    public void getRankShouldReturnPositionInSortedHighscoreListPlusOne() {
        var highscores = scoreboard.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        highscores.sort(Comparator.comparingInt(Highscore::getScore));
        Collections.reverse(highscores);
        assertEquals(scoreboard.getRank(usedHighscore.getName()), highscores.indexOf(usedHighscore) + 1 );

    }
}