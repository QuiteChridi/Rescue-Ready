package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

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

    public Result main() { return ok(main.render()); }
    public Result login() {return ok(login.render()); }
    public Result profile() { return ok(profile.render()); }
    public Result highscore() {
        return ok(highscore.render(getHighscores()));
    }

    private Map<String, Integer> getHighscores(){
        Map<String, Integer> highscores = new HashMap<>();
        highscores.put("Willi", (int) (Math.random() * 100));
        highscores.put("Hubert", (int) (Math.random() * 100));
        highscores.put("Sepp", (int) (Math.random() * 100));
        highscores.put("Fred", (int) (Math.random() * 100));
        highscores.put("Paul", (int) (Math.random() * 100));

        return highscores;
    }

    public Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        ObjectNode result = Json.newObject();

        if(username.equals("admin")){
            if(password.equals("admin")){
                result.put("response", "Login successfull");
            } else{
                result.put("response", "Wrong Password");
            }
        } else {
            result.put("response", "User not found");
        }
        return ok(result);
    }
}
