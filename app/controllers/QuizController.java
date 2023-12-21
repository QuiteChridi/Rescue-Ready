package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Database;
import models.DummyDatabase;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.quiz.question;


public class QuizController extends Controller {
    Database database = new DummyDatabase();

    public Result quiz() {
        return ok(question.render());
    }

    public Result getNextQuestion(){
        return null;
    }

    public Result checkAnswer(Http.Request request){
        return null;
    }

    public Result handleResult(Http.Request request) {
        JsonNode json = request.body().asJson();
        int highscore = json.findPath("highscore").asInt();

        String username = request.session().get("username").orElse("Guest");

        if(database.isInList(username)){
            database.updateHighscore(username, highscore);
        } else {
            database.addHighscore(username, highscore);
        }

        int rank = database.getRank(username);

        return ok().addingToSession(request, "highscore", String.valueOf(highscore)).addingToSession(request, "rank", String.valueOf(rank));
    }
}
