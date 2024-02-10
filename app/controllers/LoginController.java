package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.inject.Inject;

import controllers.interfaces.AbstractUserFactory;
import controllers.interfaces.User;
import play.libs.Json;
import play.mvc.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.UserFactory;
import views.html.login;
import views.html.signup;


public class LoginController extends Controller {

    private final AbstractUserFactory users;
    final Logger loginLogger = LoggerFactory.getLogger(this.getClass());

    @Inject
    public LoginController(UserFactory users) {
        this.users = users;
    }

    public LoginController(AbstractUserFactory users) {
        this.users = users;
    }

    public Result login() {
        return ok(login.render());
    }

    public Result signUp() {
        return ok(signup.render());
    }

    public Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();

        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        ObjectNode result = Json.newObject();

        loginLogger.debug("Attempting login check.");
        try {
            User user = users.authenticate(username, password);
            if (user != null) {
                result.put("response", "Login successful");
                // Fügen Sie die Benutzer-ID zur Session hinzu, nicht den Benutzernamen
                return ok(result).addingToSession(request, "userID", Integer.toString(user.getId()));
            } else {
                result.put("response", "Wrong username or password");
            }
        } catch (Throwable t) {
            loginLogger.error("Exception during authentication", t);
            result.put("response", "Login failed due to an error");
        }
        return ok(result);
    }

    public Result logout () {
        System.out.println("Logged out");
        return redirect("/").withNewSession();
    }

    public Result createUser(Http.Request request) {
        JsonNode json = request.body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        String email = json.findPath("email").textValue();
        ObjectNode resultBody = Json.newObject();

        try {
            if (users.createUserInUsers(username, password, email) != null) {
                resultBody.put("response", "Signup successful");
                loginLogger.debug("Username: " + username);
                loginLogger.debug("Password: " + password);
                loginLogger.debug("Email: " + email);
                return ok().addingToSession(request, "username", username);

            } else {
                resultBody.put("response", "Username already exists");
                return notAcceptable(resultBody);
            }

        } catch (Throwable t) {
            return internalServerError("Exception with username + password");
        }

    }
}
