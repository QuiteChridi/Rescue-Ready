package models;

import controllers.interfaces.Highscore;

import org.junit.Before;
import org.junit.Test;
import play.test.WithApplication;
import static org.junit.Assert.*;

public class HighscoreFactoryTest extends WithApplication {

    private HighscoreFactory highscores;
    private Highscore highscore;

    /*@Before
    public void setUp() {
        highscores = provideApplication().injector().instanceOf(HighscoreFactory.class);
    }

    @Test
    public void getNameShouldReturnName(){

        assertEquals("test", highscore.getUserName());
    }

    @Test
    public void getScoreShouldReturnScore(){
        assertEquals(123, highscore.getScore());
    }

    @Test
    public void setScoreShouldSetNewScore(){
        int newScore = 10;
        highscore.setScore(newScore);
        assertEquals(newScore, highscore.getScore());
    }

    @Test
    public void highscoresWithSameNameAndSameScoreShouldBeEqual(){
        var h1 = new HighscoreFactory.Highscore("test", 123);
        var h2 = new HighscoreFactory.Highscore("test", 123);
        assertEquals(h1, h2);
    }

    @Test
    public void highscoresWithDifferentNameShouldNotBeEqual(){
        var h1 = new HighscoreFactory.Highscore("test", 123);
        var h2 = new HighscoreFactory.Highscore("toast", 123);
        assertNotEquals(h1, h2);
    }
    @Test
    public void highscoresWithSameNameAndDifferentScoreShouldBeEqual(){
        var h1 = new HighscoreFactory.Highscore("test", 123);
        var h2 = new HighscoreFactory.Highscore("test", 122);
        assertEquals(h1, h2);
    }
    @Test
    public void compareToShouldReturnZeroForHighscoreWithSameScores(){
        var h1 = new HighscoreFactory.Highscore("test", 123);
        var h2 = new HighscoreFactory.Highscore("test", 123);
        assertEquals(0, h1.compareTo(h2));
    }
    @Test
    public void compareToShouldReturnPositiveIntForHighscoreWithHigherScores(){
        var h1 = new HighscoreFactory.Highscore("test", 123);
        var h2 = new HighscoreFactory.Highscore("test", 122);
        assertTrue(h1.compareTo(h2) < 0);
    }
    @Test
    public void compareToShouldReturnNegativeIntForHighscoreWithLowerScores(){
        var h1 = new HighscoreFactory.Highscore("test", 122);
        var h2 = new HighscoreFactory.Highscore("test", 123);
        assertTrue(h1.compareTo(h2) > 0);
    }*/
}