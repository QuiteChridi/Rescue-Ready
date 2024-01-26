package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.UserFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.shop;

import javax.inject.Inject;

public class ShopController extends Controller {

    UserFactory users;

    @Inject
    public ShopController(UserFactory users) {
        this.users = users;
    }

    private int getUserIdFromSession(Http.Request request) {
        String userIDString = request.session().get("userID").orElse(null);
        return (userIDString != null && !userIDString.equals("leer")) ? Integer.parseInt(userIDString) : -1;
    }

    private UserFactory.User getUserFromSession(Http.Request request) {
        int userID = getUserIdFromSession(request);
        return (userID != -1) ? users.getUserById(userID) : null;
    }

    public Result shop(Http.Request request) {
        UserFactory.User user = getUserFromSession(request);

        if (user != null) {
            return ok(shop.render(user));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result getAvailableCoins(Http.Request request) {
        UserFactory.User user = getUserFromSession(request);

        if (user != null) {
            int availableCoins = user.getCoins();
            System.out.println(availableCoins);
            return ok(Json.newObject().put("availableCoins", availableCoins));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result setNewCoins(Http.Request request) {
        try {
            UserFactory.User user = getUserFromSession(request);

            if (user != null) {
                JsonNode json = request.body().asJson();
                int newCoins = json.findPath("newCoins").intValue();
                System.out.println("Neue Anzahl Coins" + newCoins);
                user.setCoins(newCoins);

                ObjectNode result = Json.newObject();
                result.put("success", true);
                result.put("newCoins", newCoins);

                System.out.println("Result: " + result);
                return ok(result);
            } else {
                return redirect(routes.LoginController.login());
            }
        } catch (NumberFormatException e) {
            return redirect(routes.LoginController.login());
        }
    }
}
