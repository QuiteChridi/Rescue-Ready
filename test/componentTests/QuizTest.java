package componentTests;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.interfaces.Highscore;
import models.HighscoreFactory;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class QuizTest extends WithApplication {
    private HighscoreFactory highscoreFactory;

    @Before
    public  void setUp(){
        highscoreFactory = provideApplication().injector().instanceOf(HighscoreFactory.class);
    }

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

    @Test
    public void safeHighscoreShouldSafeHigherScore() {
        ObjectNode requestBody = Json.newObject();

        Highscore oldHighscore = highscoreFactory.getHighscoreOfUserAndQuiz(1, 1 );
        int scoreToSet = oldHighscore.getScore() + 10;
        requestBody.put("score", scoreToSet);

        Http.RequestBuilder requestWithHigherScore = fakeRequest()
                .session("userID", "1")
                .bodyJson(requestBody)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .method(POST)
                .uri("/saveQuizResult");

        Result result = route(app, requestWithHigherScore);
        Highscore newHighscore = highscoreFactory.getHighscoreOfUserAndQuiz(1, 1 );

        assertEquals(Http.Status.OK, result.status());
        assertEquals(newHighscore.getScore(), scoreToSet);
    }

    @Test
    public void safeHighscoreShouldNotSafeLowerScore() {
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
}
