package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.interfaces.AbstractJokerFactory;
import controllers.interfaces.User;
import models.JokerFactory;
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
    private final AbstractJokerFactory jokers;

    @Inject
    public InventoryController(UserFactory users, JokerFactory jokers) {
        this.users = users;
        this.jokers = jokers;
    }

    public Result shop(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        return ok(shop.render(user, jokers.getJokerById(1), jokers.getJokerById(2), jokers.getJokerById(3)));
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

    public Result setJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();
        int jokerId = json.findPath("jokerId").intValue();

        jokers.setJokersOfUser(jokerId, user.getId(), newAmountOfJokers);

        ObjectNode result = Json.newObject();
        result.put("success", true);
        result.put("newAmountOfJokers", newAmountOfJokers);
        return ok(result);
    }

    public Result getFiftyFiftyJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        ObjectNode result = Json.newObject();
        result.put("availableFiftyFiftyJoker", user.getFiftyFiftyJoker());
        return ok(result);
    }

    public Result setFiftyFiftyJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();

        user.setFiftyFiftyJoker(newAmountOfJokers);
        user.save();

        ObjectNode result = Json.newObject();
        result.put("success", true);
        result.put("newAmountOfJokers", user.getFiftyFiftyJoker());
        return ok(result);
    }

    public Result getPauseJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        ObjectNode result = Json.newObject();
        result.put("availablePauseJoker", user.getPauseJoker());
        return ok(result);
    }

    public Result setPauseJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();

        user.setPauseJoker(newAmountOfJokers);
        user.save();

        ObjectNode result = Json.newObject();
        result.put("success", true);
        result.put("newAmountOfJokers", user.getPauseJoker());
        return ok(result);
    }

    public Result getDoublePointsJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        ObjectNode result = Json.newObject();
        result.put("availableDoublePointsJoker", user.getDoublePointsJoker());
        return ok(result);
    }

    public Result setDoublePointsJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();

        user.setDoublePointsJoker(newAmountOfJokers);
        user.save();

        ObjectNode result = Json.newObject();
        result.put("success", true);
        result.put("newAmountOfJokers", user.getDoublePointsJoker());
        return ok(result);
    }

    private User getUserFromSession(Http.Request request){
        return request
                .session()
                .get("userID")
                .map(Integer::parseInt)
                .map(users::getUserById)
                .orElse(null);
    }
}
