package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import controllers.interfaces.*;
import play.mvc.*;

import models.HighscoreFactory;
import models.QuizFactory;
import views.html.highscore;

import java.util.List;


public class ScoreboardController extends Controller {
    private final AbstractQuizFactory quizzes;
    private final AbstractHighscoreFactory scores;
    private List<Highscore> currentHighscoreList;

    @Inject
    public ScoreboardController(QuizFactory quizzes, HighscoreFactory scores) {
        this.quizzes = quizzes;
        this.scores = scores;
        currentHighscoreList = scores.getHighscoresOfQuiz(1);
    }

    public Result highscore(){
        return ok(highscore.render(currentHighscoreList, quizzes.getPossibleQuizNames()));
    }

    public  Result renderHighscore(Http.Request request){
        JsonNode json = request.body().asJson();
        int quizId = json.findPath("quizId").asInt();
        currentHighscoreList = scores.getHighscoresOfQuiz(quizId);
        currentHighscoreList.sort(Highscore::compareTo);

        return ok(highscore.render(currentHighscoreList, quizzes.getPossibleQuizNames()));
    }
}
