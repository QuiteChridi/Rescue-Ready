package models;

import controllers.interfaces.Highscore;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.test.WithApplication;
import static org.junit.Assert.*;

public class HighscoreImplementationTest extends WithApplication {

    private static int TEST_SCORE = 100;
    private static int HIGHER_SCORE = 110;
    private static int LOWER_SCORE = 90;
    private static String TEST_USER_NAME = "willi";
    private static String TEST_QUIZ_NAME = "someQuiz";
    private static String TEST_PROFILE_PIC_PATH = "/public/ProfilePic.png";
    private static int TEST_USER_ID = 1;
    private static int TEST_QUIZ_ID = 2;
    private Highscore highscore;
    private Highscore higherHighscore;
    private Highscore lowerHighscore;

    @Before public void setUp(){
        highscore = new HighscoreFactory.HighscoreImplementation(TEST_SCORE, TEST_QUIZ_ID, TEST_USER_ID, TEST_QUIZ_NAME, TEST_USER_NAME, TEST_PROFILE_PIC_PATH);
        higherHighscore = new HighscoreFactory.HighscoreImplementation(HIGHER_SCORE, TEST_QUIZ_ID, TEST_USER_ID, TEST_QUIZ_NAME, TEST_USER_NAME, TEST_PROFILE_PIC_PATH);
        lowerHighscore = new HighscoreFactory.HighscoreImplementation(LOWER_SCORE, TEST_QUIZ_ID, TEST_USER_ID, TEST_QUIZ_NAME, TEST_USER_NAME, TEST_PROFILE_PIC_PATH);

    }

    @Test
    public void getQuizIdShouldReturnQuizId(){
        assertEquals(TEST_QUIZ_ID, highscore.getQuizId());
    }

    @Test
    public void getUserIdShouldReturnQuizId(){
        assertEquals(TEST_QUIZ_ID, highscore.getQuizId());
    }

    @Test
    public void getScoreShouldReturnScore(){
        assertEquals(TEST_SCORE, highscore.getScore());
    }

    @Test
    public void getProfilePicPathShouldReturnProfilePicPath(){
        assertEquals(TEST_PROFILE_PIC_PATH, highscore.getProfilePicPath());
    }

    @Test
    public void getUserNameShouldReturnName(){
        assertEquals(TEST_USER_NAME, highscore.getUserName());
    }

    @Test
    public void getQuizNameShouldReturnQuizName(){
        assertEquals(TEST_QUIZ_NAME, highscore.getQuizName());
    }

    @Test
    public void compareToShouldReturnZeroForHighscoreWithSameScores(){
        assertEquals(0, highscore.compareTo(highscore));
    }
    @Test
    public void compareToShouldReturnPositiveIntForHighscoreWithHigherScores(){
        assertTrue(highscore.compareTo(higherHighscore) > 0);
    }
    @Test
    public void compareToShouldReturnNegativeIntForHighscoreWithLowerScores(){
        assertTrue(highscore.compareTo(lowerHighscore) < 0);
    }
}