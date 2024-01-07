package controllers;

import controllers.interfaces.Scoreboard;
import models.*;
import play.mvc.*;
import views.html.*;


public class HomeController extends Controller {
    Scoreboard scoreboard = new DummyScoreboard();

    public Result main() {
        return ok(main.render());
    }

    public Result profile() {
        return ok(profile.render());
    }

    public Result highscore() {
        return ok(highscore.render(scoreboard.getHighscores()));
    }

    public Result signup() {
        return ok(signup.render());
    }

    public Result getHighscoreFromSession(Http.Request request) {
        String highscore = request.session().get("highscore").orElse("0");
        return ok(highscore);
    }
}