package integrationTests;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.QuizController;
import controllers.interfaces.Highscore;
import models.HighscoreFactory;
import models.QuizFactory;
import models.UserFactory;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class QuizControllerTest extends WithApplication {
    private QuizController quizController;
    private HighscoreFactory highscoreFactory;

    @Before
    public  void setUp(){
        UserFactory userFactory = provideApplication().injector().instanceOf(UserFactory.class);
        highscoreFactory = provideApplication().injector().instanceOf(HighscoreFactory.class);
        QuizFactory quizFactory = provideApplication().injector().instanceOf(QuizFactory.class);

        quizController = new QuizController(quizFactory, userFactory, highscoreFactory);
    }

    @Test
    public void safeHighscoreShouldSafeHigherScore() {
        ObjectNode requestBody = Json.newObject();

        Highscore oldHighscore = highscoreFactory.getHighscoreOfUserAndQuiz(1, 1 );
        int scoreToSet = oldHighscore.getScore() + 10;
        quizController.setQuiz(1);
        requestBody.put("score", scoreToSet);

        Http.RequestBuilder requestWithHigherScore = fakeRequest()
                .session("userID", "1")
                .bodyJson(requestBody)
                .method("POST")
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
        quizController.setQuiz(1);
        requestBody.put("score", scoreToSet);

        Http.RequestBuilder requestWithHigherScore = fakeRequest()
                .session("userID", "1")
                .bodyJson(requestBody)
                .method("POST")
                .uri("/saveQuizResult");

        Result result = route(app, requestWithHigherScore);
        Highscore newHighscore = highscoreFactory.getHighscoreOfUserAndQuiz(1, 1 );

        assertEquals(Http.Status.OK, result.status());
        assertNotEquals(newHighscore.getScore(), scoreToSet);
    }
}
