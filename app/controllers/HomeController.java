package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.DummyDatabase;
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
        return ok(highscore.render(DummyDatabase.getHighscores()));
    }

    private Result saveHighscore(Http.Request request){
        JsonNode json = request.body().asJson();
        int highscore = Integer.parseInt(json.findPath("highscore").textValue());

        String username = request.session().get("username").orElse("Guest");

        DummyDatabase.updateHighscore(username, highscore);

        return ok().addingToSession(request, "higscore", String.valueOf(highscore));
    }

    private int computeRank(){
       return 0;
    }

    public Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();

        ObjectNode result = Json.newObject();

        if(username.equals("admin")){
            if(password.equals("admin")){
                result.put("response", "Login successful");
            } else{
                result.put("response", "Wrong Password");
            }
        } else {
            result.put("response", "User not found");
        }
        return ok(result).addingToSession(request, "username", "admin");
    }

    public Result logout(Http.Request request){
        return redirect("/login").withNewSession();
    }

    public Result signup() {
        return ok(signup.render());
    }
}
