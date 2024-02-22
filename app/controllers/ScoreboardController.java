package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import controllers.interfaces.*;
import play.mvc.*;

import models.HighscoreFactory;
import views.html.highscore;

import java.util.List;


public class ScoreboardController extends Controller {
    private final AbstractHighscoreFactory scores;

    @Inject
    public ScoreboardController(HighscoreFactory scores) {
        this.scores = scores;
    }

    public Result highscore(int quizId){
        List <Highscore> currentHighscoreList = scores.getHighscoresOfQuiz(quizId);
        currentHighscoreList.sort(Highscore::compareTo);

        return ok(highscore.render(currentHighscoreList, scores.getPossibleQuizNames()));
    }

    public  Result renderHighscore(Http.Request request){
        JsonNode json = request.body().asJson();
        int quizId = json.findPath("quizId").asInt();

        List <Highscore> currentHighscoreList = scores.getHighscoresOfQuiz(quizId);
        currentHighscoreList.sort(Highscore::compareTo);
        return redirect(routes.ScoreboardController.highscore(quizId));
    }
}
