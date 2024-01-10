package models;

import org.junit.Test;

import javax.validation.constraints.AssertFalse;

import static org.junit.Assert.*;

public class HighscoreTest {

    @Test
    public void getNameShouldReturnName(){
        var highscore = new Highscore("test", 123);
        assertEquals("test", highscore.getName());
    }

    @Test
    public void getScoreShouldReturnScore(){
        var highscore = new Highscore("test", 123);
        assertEquals(123, highscore.getScore());
    }

    @Test
    public void setScoreShouldSetNewScore(){
        var highscore = new Highscore("test", 123);
        highscore.setScore(10);
        assertEquals(10, highscore.getScore());
    }

    @Test
    public void highscoresWithSameNameAndSameScoreShouldBeEqual(){
        var h1 = new Highscore("test", 123);
        var h2 = new Highscore("test", 123);
        assertEquals(h1, h2);
    }

    @Test
    public void highscoresWithDifferentNameShouldNotBeEqual(){
        var h1 = new Highscore("test", 123);
        var h2 = new Highscore("toast", 123);
        assertNotEquals(h1, h2);
    }
    @Test
    public void highscoresWithSameNameAndDifferentScoreShouldBeEqual(){
        var h1 = new Highscore("test", 123);
        var h2 = new Highscore("test", 122);
        assertEquals(h1, h2);
    }
    @Test
    public void compareToShouldReturnZeroForHighscoreWithSameScores(){
        var h1 = new Highscore("test", 123);
        var h2 = new Highscore("test", 123);
        assertEquals(0, h1.compareTo(h2));
    }
    @Test
    public void compareToShouldReturnPositiveIntForHighscoreWithHigherScores(){
        var h1 = new Highscore("test", 123);
        var h2 = new Highscore("test", 122);
        assertTrue(h1.compareTo(h2) < 0);
    }
    @Test
    public void compareToShouldReturnNegativeIntForHighscoreWithLowerScores(){
        var h1 = new Highscore("test", 122);
        var h2 = new Highscore("test", 123);
        assertTrue(h1.compareTo(h2) > 0);
    }
}