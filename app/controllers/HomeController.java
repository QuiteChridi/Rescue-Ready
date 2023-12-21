package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.mvc.*;
import views.html.*;


public class HomeController extends Controller {
    Database database = new DummyDatabase();

    public Result main() {
        return ok(main.render());
    }

    public Result profile() {
        return ok(profile.render());
    }

    public Result highscore() {
        return ok(highscore.render(database.getHighscores()));
    }

    public Result signup() {
        return ok(signup.render());
    }

    public Result getHighscoreFromSession(Http.Request request) {
        String highscore = request.session().get("highscore").orElse("0");
        return ok(highscore);
    }
}