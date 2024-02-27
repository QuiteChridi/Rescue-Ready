package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.inject.Inject;

import controllers.interfaces.AccountManager;
import controllers.interfaces.User;
import play.libs.Json;
import play.mvc.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import models.UserFactory;
import views.html.login;
import views.html.signup;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's login page.
 * It extends the Controller class provided by Play.
 */
public class LoginController extends Controller {

    /**
     * The accounts is used to manage the users.
     */
    private final AccountManager accounts;

    /**
     * The loginLogger is used to log the login attempts.
     */
    final Logger loginLogger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructor for the LoginController
     *
     * @param accounts the UserFactory
     * @Inject is used to inject the UserFactory
     */
    @Inject
    public LoginController(UserFactory accounts) {
        this.accounts = accounts;
    }

    /**
     * Renders the login page.
     *
     * @return the login page
     */
    public Result login() {
        return ok(login.render());
    }

    /**
     * Renders the signup page
     *
     * @return the signup page
     */
    public Result signUp() {
        return ok(signup.render());
    }

    /**
     * Authenticates the user. This method is called when the user tries to log in. It checks if the username and password are correct.
     * If the username and password are correct, the user is logged in and the user's ID is added to the session.
     *
     * @param request the HTTP request
     * @return the result of the authentication
     */
    public Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();

        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        ObjectNode result = Json.newObject();

        loginLogger.debug("Attempting login check.");
        try {
            User user = accounts.authenticate(username, password);
            if (user != null) {
                result.put("response", "Login successful");
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

    /**
     * Logs the user out. This method is called when the user tries to log out. It removes the user's ID from the session.
     *
     * @return the result of the logout
     */
    public Result logout() {
        System.out.println("Logged out");
        return redirect("/").withNewSession();
    }

    /**
     * Creates a new user. This method is called when the user tries to sign up. It checks if the username is already taken.
     * If the username is not taken, the user is created and logged in.
     *
     * @param request the HTTP request
     * @return the result of the user creation
     */
    public Result createUser(Http.Request request) {
        JsonNode json = request.body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        String email = json.findPath("email").textValue();
        ObjectNode resultBody = Json.newObject();

        try {
            User newUser = accounts.createUser(username, password, email);
            if (newUser != null) {
                resultBody.put("response", "Signup successful");
                return ok(resultBody).addingToSession(request, "userID", Integer.toString(newUser.getId()));
            } else {
                resultBody.put("response", "Username already exists");
                return notAcceptable(resultBody);
            }
        } catch (Throwable t) {
            loginLogger.error("Exception during user creation", t);
            return internalServerError("Exception with username + password");
        }
    }
}
