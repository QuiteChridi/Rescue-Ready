package models;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class DummyScoreboardTest {

    @Test
    public void getHighscoresShouldReturnACopyOfHighscores(){
        var database = new DummyScoreboard();
        var highscores = database.getHighscores();
        highscores.add(new Highscore("test", 123));
        assertNotEquals(highscores, database.getHighscores());
    }
    @Test
    public void updateHighscoreShouldUpdateHighscoreInList() {
        var database = new DummyScoreboard();
        var highscores = database.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        int scoreToSet = usedHighscore.getScore() + 12;
        database.updateHighscore(usedHighscore.getName(), scoreToSet);
        assertEquals(scoreToSet, database.getHighscore(usedHighscore.getName()).getScore());
    }
    @Test
    public void updateHighscoreShouldThrowIfHighscoreIsNotInList() {
        var database = new DummyScoreboard();
        assertThrows(IllegalArgumentException.class, () -> database.updateHighscore("", 20));
    }

    @Test
    public void isInHighscoreListShouldReturnTrueForHighscoreInList() {
        var database = new DummyScoreboard();
        var highscores = database.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        assertTrue(database.isInHighscoreList(usedHighscore.getName()));
    }

    @Test
    public void isInHighscoreListShouldReturnFalseForHighscoreNotInList() {
        var database = new DummyScoreboard();
        assertFalse(database.isInHighscoreList(""));
    }

    @Test
    public void getHighscoreShouldReturnTheHighscoreWithThePassedName(){
        var database = new DummyScoreboard();
        var highscores = database.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        assertEquals(usedHighscore, database.getHighscore(usedHighscore.getName()));
    }

    @Test
    public void getHighscoreShouldReturnNullIfNameNotFound(){
        var database = new DummyScoreboard();
        assertNull(database.getHighscore(""));
    }
    @Test
    public void highscoreListShouldContainHighscoreAfterAdding() {
        var database = new DummyScoreboard();
        database.addHighscore("test", 123);
        assertTrue(database.getHighscores().contains(new Highscore("test", 123)));
    }

    @Test
    public void getRankShouldReturnPositionInSortedHighscoreListPlusOne() {
        var database = new DummyScoreboard();
        var highscores = database.getHighscores();
        int randomHighscoreIndex = (int) (Math.random() * (highscores.size()-1));
        Highscore usedHighscore = highscores.get(randomHighscoreIndex);
        highscores.sort(Comparator.comparingInt(Highscore::getScore));
        assertEquals(database.getRank(usedHighscore.getName()), highscores.indexOf(usedHighscore) + 1 );

    }
}