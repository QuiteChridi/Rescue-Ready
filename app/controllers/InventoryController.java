package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.interfaces.AbstractJokerFactory;
import controllers.interfaces.Joker;
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
import java.util.List;


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

        List<Integer> allJokerAmounts = jokers.getAllJokerAmountsOfUser(user.getId());
        Joker fiftyFifty = jokers.getJokerById(1);
        Joker pauseJoker = jokers.getJokerById(2);
        Joker doublePoints = jokers.getJokerById(3);
        return ok(shop.render(user, fiftyFifty, pauseJoker, doublePoints, allJokerAmounts));
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

    public Result buyJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int jokerId = json.findPath("jokerId").intValue();
        int oldAmountOfJokers = jokers.getJokerAmountOfUser(jokerId, user.getId());
        int coins = user.getCoins();
        int price = jokers.getJokerById(jokerId).getPrice();
        System.out.println(jokerId);
        ObjectNode result = Json.newObject();
        if(coins >= price){
            int newAmountOfJokers = oldAmountOfJokers + 1;
            int newAmountOfCoins = coins - price;
            jokers.setJokerAmountOfUser(jokerId, user.getId(), newAmountOfJokers);
            user.setCoins(newAmountOfCoins);
            user.save();
            result.put("success", true);
            result.put("newAmountOfJokers", newAmountOfJokers);
            result.put("newAmountOfCoins", newAmountOfCoins);
        } else {
            result.put("success", false);
        }
        return ok(result);
    }

    public Result useJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int jokerId = json.findPath("jokerId").intValue();
        int newAmountOfJokers = jokers.getJokerAmountOfUser(jokerId, user.getId());
        jokers.setJokerAmountOfUser(jokerId, user.getId(), newAmountOfJokers);

        ObjectNode result = Json.newObject();
        result.put("success", true);
        result.put("newAmountOfJokers", newAmountOfJokers);
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
