package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController extends Controller {
    final Logger loginLogger = LoggerFactory.getLogger(this.getClass());

    public LoginController() {
    }

    public Result login() {
        return ok(login.render());
    }

    public Result authenticate(Http.Request request) {
        JsonNode json = request.body().asJson();
        String username = json.findPath("username").textValue();
        String password = json.findPath("password").textValue();
        ObjectNode result = Json.newObject();

        loginLogger.debug("Attempting login check.");
        try {
            if (username.equals("admin")) {
                loginLogger.debug("Username: " + username);
                if (password.equals("admin")) {
                    result.put("response", "Login successful");
                    loginLogger.debug("Password: " + password);
                } else {
                    result.put("response", "Wrong Password");
                }
            } else {
                result.put("response", "User not found");
            }
            System.out.println("Username: " + username + "\nPassword: " + password);
        } catch (Throwable t) {
            loginLogger.error("Exception with username + password", t);
        }
        return ok(result).addingToSession(request, "username", "admin");
    }

        public Result logout () {
            System.out.println("Logged out");
            return redirect("/").withNewSession();
        }


    }
