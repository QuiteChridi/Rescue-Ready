package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import controllers.interfaces.*;
import play.mvc.*;

import models.HighscoreFactory;
import views.html.highscore;


import java.util.List;

/**
 * This class is the controller for the scoreboard page. It is responsible for handling the scoreboard logic and
 * rendering the scoreboard page.
 */
public class ScoreboardController extends Controller {
    private final AbstractHighscoreFactory scores;

    /**
     * Constructor for the ScoreboardController. Injects the HighscoreFactory.
     * @param scores The HighscoreFactory to get the highscores from.
     */
    @Inject
    public ScoreboardController(HighscoreFactory scores) {
        this.scores = scores;
    }

    /**
     * Renders the highscore page.
     * @param quizId The id of the quiz to get the highscores from.
     * @return The highscore page.
     */
    public Result highscore(int quizId){
        List <Highscore> currentHighscoreList = scores.getHighscoresOfQuiz(quizId);
        currentHighscoreList.sort(Highscore::compareTo);

        return ok(highscore.render(currentHighscoreList, scores.getPossibleQuizNames()));
    }

    /**
     * Renders the highscore page.
     * @param request The request object.
     * @return The highscore page.
     */
    public  Result renderHighscore(Http.Request request){
        JsonNode json = request.body().asJson();
        int quizId = json.findPath("quizId").asInt();

        List <Highscore> currentHighscoreList = scores.getHighscoresOfQuiz(quizId);
        currentHighscoreList.sort(Highscore::compareTo);
        return redirect(routes.ScoreboardController.highscore(quizId));
    }
}
