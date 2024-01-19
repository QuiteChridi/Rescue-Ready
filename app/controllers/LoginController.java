package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.UserFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController extends Controller {

    UserFactory users;

    @Inject
    public LoginController(UserFactory users) {
        this.users = users;
    }

    final Logger loginLogger = LoggerFactory.getLogger(this.getClass());

    public Result login() {
        return ok(login.render());
    }

    public Result signUp() {
        return ok(views.html.signup.render());
    }

    public Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        ObjectNode result = Json.newObject();

        loginLogger.debug("Attempting login check.");
        try {
            if (users.authenticate(username, password) != null) {
                result.put("response", "Login successful");
                loginLogger.debug("Username: " + username);
                loginLogger.debug("Password: " + password);
            } else {
                result.put("response", "Wrong Password");
            }
            System.out.println("Username: " + username + "\nPassword: " + password);
        } catch (Throwable t) {
            loginLogger.error("Exception with username + password", t);
        }
        return ok(result).addingToSession(request, "username", username);
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
        ObjectNode result = Json.newObject();

        loginLogger.debug("Attempting sign up.");
        try {
            if (users.signUp(username, password, email) != null) {
                result.put("response", "Sign up successful");
                loginLogger.debug("Username: " + username);
                loginLogger.debug("Password: " + password);
                loginLogger.debug("Email: " + email);
            } else {
                result.put("response", "Username already exists");
            }
            System.out.println("Username: " + username + "\nPassword: " + password + "\nEmail: " + email);
        } catch (Throwable t) {
            loginLogger.error("Exception with username + password", t);
        }
        return ok(result).addingToSession(request, "username", username);
    }
}
