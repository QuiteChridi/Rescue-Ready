package componentTests;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.interfaces.Highscore;
import models.HighscoreFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

/**
 * This class contains tests for the QuizController.
 * The tests are written using the JUnit testing framework.
 * It extends the WithApplication class to provide a fake application for the tests.
 */
public class QuizTest extends WithApplication {

    /**
     * This test checks if the quiz page is returned when the quiz method is called.
     */
    @Test
    public void simulationOfPlayingQuiz() {
        Http.RequestBuilder request = fakeRequest()
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .session("userID", "1")
                .method(GET)
                .uri("/quiz");

        Result result = route(app, request);
        assertEquals(Http.Status.OK, result.status());

        while(!contentAsString(result).contains("endOfQuiz")){
            request = fakeRequest()
                    .header(Http.HeaderNames.HOST, "localhost:19001")
                    .session("userID", "1")
                    .method(GET)
                    .uri("/getNextQuestion");

            result = route(app, request);
            assertEquals(Http.Status.OK, result.status());

            if(!contentAsString(result).contains("endOfQuiz")){
                assertTrue(contentAsString(result).contains("question"));
                assertTrue(contentAsString(result).contains("answers"));
            }
        }
        assertFalse(contentAsString(result).contains("question"));
        assertFalse(contentAsString(result).contains("answers"));
    }

    /**
     * This test checks if the highscore page is returned when the highscore method is called.
     */
    @Test
    public void safeHighscoreShouldNotSafeLowerScore() {
        HighscoreFactory highscoreFactory = provideApplication().injector().instanceOf(HighscoreFactory.class);
        ObjectNode requestBody = Json.newObject();

        Highscore oldHighscore = highscoreFactory.getHighscoreOfUserAndQuiz(1, 1 );
        int scoreToSet = oldHighscore.getScore() - 10;
        requestBody.put("score", scoreToSet);

        Http.RequestBuilder requestWithHigherScore = fakeRequest()
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .session("userID", "1")
                .bodyJson(requestBody)
                .method(POST)
                .uri("/saveQuizResult");

        Result result = route(app, requestWithHigherScore);
        Highscore newHighscore = highscoreFactory.getHighscoreOfUserAndQuiz(1, 1 );

        assertEquals(Http.Status.OK, result.status());
        assertNotEquals(newHighscore.getScore(), scoreToSet);
    }

    /**
     * This test checks if the highscore page is returned when the highscore method is called.
     */
    @Test
    public void safeHighscoreShouldSafeHigherScore() {
        HighscoreFactory highscoreFactory = provideApplication().injector().instanceOf(HighscoreFactory.class);
        ObjectNode requestBody = Json.newObject();

        Highscore oldHighscore = highscoreFactory.getHighscoreOfUserAndQuiz(1, 1);
        int scoreToSet = oldHighscore.getScore() + 10;
        requestBody.put("score", scoreToSet);

        Http.RequestBuilder requestWithHigherScore = fakeRequest()
                .session("userID", "1")
                .bodyJson(requestBody)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .method(POST)
                .uri("/saveQuizResult");

        Result result = route(app, requestWithHigherScore);
        Highscore newHighscore = highscoreFactory.getHighscoreOfUserAndQuiz(1, 1);

        assertEquals(Http.Status.OK, result.status());
        assertEquals(newHighscore.getScore(), scoreToSet);
    }
}
