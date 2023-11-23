package controllers;

import play.mvc.*;

import java.util.HashMap;
import java.util.Map;

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
    public Result main() { return ok(views.html.main.render()); }
    public Result login() { return ok(views.html.login.render()); }
    public Result profile() { return ok(views.html.profile.render()); }
    public Result highscore() {
        Map<String, Integer> highscores = new HashMap<>();
        highscores.put("Willi", 0);
        highscores.put("Hubert", 2);
        highscores.put("Sepp", 0);
        highscores.put("Fred", 0);
        highscores.put("Paul", 0);

        return ok(views.html.highscore.render(highscores)); }
}
