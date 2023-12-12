package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.libs.Json;
import play.mvc.Result;

import java.net.http.HttpRequest;


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
    public Result index() { return ok(views.html.login.render()); }
    public Result main() { return ok(views.html.main.render()); }
    public Result login() { return ok(views.html.login.render()); }
    public Result profile() { return ok(views.html.profile.render()); }
    public Result highscore() { return ok(views.html.highscore.render()); }

    public Result validateLogin(Http.Request request){
        JsonNode json = request.body().asJson();
        String username = json.get("username").asText();
        String password = json.get("password").asText();

        if (username.equals("admin") && password.equals("admin")) {
            request.session();
            return ok("Authenticated");

        } else {
            return badRequest("Invalid username or password");
        }
    }
    public Result logout(Http.Request request){
        request.session().data().clear();
        return redirect("/login");
    }
}
