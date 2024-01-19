package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.interfaces.Scoreboard;
import models.*;
import play.mvc.*;
import views.html.*;

import java.util.List;


public class HomeController extends Controller {
     Scoreboard scoreboard = DummyScoreboard.getInstance();
     private List<Highscore> currentHighscoreList;


    public Result main() {
        return ok(main.render());
    }

    public Result profile() {
        return ok(profile.render(DummyFriendList.getUserList()));
    }

    public Result highscore(){
        return ok(highscore.render(scoreboard.getHighscores(),QuizFactory.getPossibleQuizes()));
    }

    public  Result renderHighscore(Http.Request request){
        JsonNode json = request.body().asJson();
        String quizName = json.findPath("quizName").asText();
        currentHighscoreList= DummyScoreboard.getInstance().getHighscores();

        return ok(highscore.render(currentHighscoreList,QuizFactory.getPossibleQuizes()));
    }

    public Result signup() {
        return ok(signup.render());
    }

    public Result getHighscoreFromSession(Http.Request request) {
        String highscore = request.session().get("highscore").orElse("0");
        return ok(highscore);
    }

    public Result friendProfile(String name) {
        String friendName = name;
        return ok(friendProfile.render(friendName));
    }
}