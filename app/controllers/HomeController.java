package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.DummyDatabase;
import play.mvc.*;
import views.html.*;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    public Result main() {
        return ok(main.render());
    }

    public Result profile() {
        return ok(profile.render());
    }

    public Result highscore() {
        return ok(highscore.render(DummyDatabase.getHighscores()));
    }

    public Result handleResult(Http.Request request) {
        JsonNode json = request.body().asJson();
        int highscore = json.findPath("highscore").asInt();

        String username = request.session().get("username").orElse("Guest");
        if(DummyDatabase.isInList(username)){
            DummyDatabase.updateHighscore(username, highscore);
        } else {
            DummyDatabase.addHighscore(username, highscore);
        }
        int rank = DummyDatabase.getRank(username);

        return ok().addingToSession(request, "highscore", String.valueOf(highscore)).addingToSession(request, "rank", String.valueOf(rank));
    }

    public Result signup() {
        return ok(signup.render());
    }

    public Result getHighscoreFromSession(Http.Request request) {
        String highscore = request.session().get("highscore").orElse("0");
        return ok(highscore);
    }
}