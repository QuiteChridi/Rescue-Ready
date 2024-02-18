package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.interfaces.User;
import models.UserFactory;
import controllers.interfaces.AbstractUserFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.shop;

import javax.inject.Inject;

public class InventoryController extends Controller {

    private final AbstractUserFactory users;

    @Inject
    public InventoryController(UserFactory users) {
        this.users = users;
    }

    public Result shop(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        return ok(shop.render(user));
    }

    public Result getCoins(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        ObjectNode result = Json.newObject();
        result.put("availableCoins", user.getCoins());
        return ok(result);
    }

    public Result setCoins(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int newCoins = json.findPath("newCoins").intValue();

        user.setCoins(newCoins);
        user.save();

        ObjectNode result = Json.newObject();
        result.put("success", true);
        result.put("newCoins", newCoins);

        return ok(result);
    }

    public Result getFiftyFiftyJoker(Http.Request request) {
        User user = getUserFromSession(request);

        if (user != null) {
            int availableFiftyFiftyJoker = user.getFiftyFiftyJoker();
            return ok(Json.newObject().put("availableFiftyFiftyJoker", availableFiftyFiftyJoker));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result setFiftyFiftyJoker(Http.Request request) {
        User user = getUserFromSession(request);

        if (user != null) {
            JsonNode json = request.body().asJson();
            System.out.println(json);
            int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();
            System.out.println(newAmountOfJokers);
            user.setFiftyFiftyJoker(newAmountOfJokers);
            user.save();

            ObjectNode result = Json.newObject();
            result.put("success", true);
            result.put("newAmountOfJokers", newAmountOfJokers);
            return ok(result);
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result getPauseJoker(Http.Request request) {
        User user = getUserFromSession(request);

        if (user != null) {
            int availablePauseJoker = user.getPauseJoker();
            return ok(Json.newObject().put("availablePauseJoker", availablePauseJoker));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result setPauseJoker(Http.Request request) {
        User user = getUserFromSession(request);

        if (user != null) {
            JsonNode json = request.body().asJson();
            int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();
            user.setPauseJoker(newAmountOfJokers);
            user.save();

            ObjectNode result = Json.newObject();
            result.put("success", true);
            result.put("newAmountOfJokers", newAmountOfJokers);
            return ok(result);
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result getDoublePointsJoker(Http.Request request) {
        User user = getUserFromSession(request);

        if (user != null) {
            int availableDoublePointsJoker = user.getDoublePointsJoker();
            return ok(Json.newObject().put("availableDoublePointsJoker", availableDoublePointsJoker));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result setDoublePointsJoker(Http.Request request) {
        User user = getUserFromSession(request);

        if (user != null) {
            JsonNode json = request.body().asJson();
            int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();
            user.setDoublePointsJoker(newAmountOfJokers);
            user.save();

            ObjectNode result = Json.newObject();
            result.put("success", true);
            result.put("newAmountOfJokers", newAmountOfJokers);
            return ok(result);
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    private User getUserFromSession(Http.Request request) {
        return request
                .session()
                .get("userID")
                .map(Integer::parseInt)
                .map(users::getUserById)
                .orElse(null);
    }
}
