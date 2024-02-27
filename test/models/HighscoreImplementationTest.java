package models;

import controllers.interfaces.Highscore;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.test.WithApplication;

import static org.junit.Assert.*;

/**
 * Testclass for HighscoreFactory
 */
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

    /**
     * Set up the highscores for the tests
     */
    @Before
    public void setUp() {
        highscore = new HighscoreFactory.HighscoreImplementation(TEST_SCORE, TEST_QUIZ_ID, TEST_USER_ID, TEST_QUIZ_NAME, TEST_USER_NAME, TEST_PROFILE_PIC_PATH);
        higherHighscore = new HighscoreFactory.HighscoreImplementation(HIGHER_SCORE, TEST_QUIZ_ID, TEST_USER_ID, TEST_QUIZ_NAME, TEST_USER_NAME, TEST_PROFILE_PIC_PATH);
        lowerHighscore = new HighscoreFactory.HighscoreImplementation(LOWER_SCORE, TEST_QUIZ_ID, TEST_USER_ID, TEST_QUIZ_NAME, TEST_USER_NAME, TEST_PROFILE_PIC_PATH);

    }

    /**
     * The quiz id should be returned by getQuizId
     */
    @Test
    public void getQuizIdShouldReturnQuizId() {
        assertEquals(TEST_QUIZ_ID, highscore.getQuizId());
    }

    /**
     * The get user id should return the user id
     */
    @Test
    public void getUserIdShouldReturnQuizId() {
        assertEquals(TEST_QUIZ_ID, highscore.getQuizId());
    }

    /**
     * The score should be returned by getScore
     */
    @Test
    public void getScoreShouldReturnScore() {
        assertEquals(TEST_SCORE, highscore.getScore());
    }

    /**
     * The profile pic path should be returned by getProfilePicPath
     */
    @Test
    public void getProfilePicPathShouldReturnProfilePicPath() {
        assertEquals(TEST_PROFILE_PIC_PATH, highscore.getProfilePicPath());
    }

    /**
     * The user name should be returned by getUserName
     */
    @Test
    public void getUserNameShouldReturnName() {
        assertEquals(TEST_USER_NAME, highscore.getUserName());
    }

    /**
     * The quiz name should be returned by getQuizName
     */
    @Test
    public void getQuizNameShouldReturnQuizName() {
        assertEquals(TEST_QUIZ_NAME, highscore.getQuizName());
    }

    /**
     * The compareTo method should return 0 for highscores with the same score
     */
    @Test
    public void compareToShouldReturnZeroForHighscoreWithSameScores() {
        assertEquals(0, highscore.compareTo(highscore));
    }

    /**
     * The compareTo method should return a positive int for highscores with higher scores
     */
    @Test
    public void compareToShouldReturnPositiveIntForHighscoreWithHigherScores() {
        assertTrue(highscore.compareTo(higherHighscore) > 0);
    }

    /**
     * The compareTo method should return a negative int for highscores with lower scores
     */
    @Test
    public void compareToShouldReturnNegativeIntForHighscoreWithLowerScores() {
        assertTrue(highscore.compareTo(lowerHighscore) < 0);
    }
}