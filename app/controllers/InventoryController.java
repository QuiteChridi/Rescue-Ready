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

/**
 * This controller contains an action to handle HTTP requests
 * to the application's inventory page.
 * It extends the Controller class provided by Play.
 */
public class InventoryController extends Controller {

    /**
     * The userFactory is used to manage the users.
     */
    private final AbstractUserFactory users;

    /**
     * The jokers is used to manage the jokers.
     */
    private final AbstractJokerFactory jokers;

    /**
     * The constructor is used to inject the userFactory and the jokers.
     *
     * @param users  The userFactory to be injected.
     * @param jokers The jokers to be injected.
     * @Inject is used to inject the userFactory and the jokers.
     */
    @Inject
    public InventoryController(UserFactory users, JokerFactory jokers) {
        this.users = users;
        this.jokers = jokers;
    }

    /**
     * This method is used to render the inventory page.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then returns the inventory page.
     *
     * @param request The HTTP request.
     * @return The inventory page.
     */
    public Result shop(Http.Request request) {
        User user = getUserFromSession(request);
        if (user == null) return redirect(routes.LoginController.login());

        List<Joker> allJokers = jokers.getAllJokers(user.getId());
        return ok(shop.render(user, allJokers));
    }

    /**
     * This method is used to get the coins of the user.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then returns the coins of the user.
     *
     * @param request The HTTP request.
     * @return The coins of the user.
     */
    public Result getCoins(Http.Request request) {
        User user = getUserFromSession(request);
        if (user == null) return redirect(routes.LoginController.login());

        ObjectNode result = Json.newObject();
        result.put("availableCoins", user.getCoins());
        return ok(result);
    }

    /**
     * This method is used to set the coins of the user.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then sets the coins of the user and returns a message.
     *
     * @param request The HTTP request.
     * @return A message.
     */
    public Result setCoins(Http.Request request) {
        User user = getUserFromSession(request);
        if (user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int newCoins = json.findPath("newCoins").intValue();

        user.setCoins(newCoins);
        user.save();

        ObjectNode result = Json.newObject();
        result.put("success", true);
        result.put("newCoins", newCoins);

        return ok(result);
    }

    /**
     * This method is used to buy a joker.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then buys the joker and returns a message.
     *
     * @param request The HTTP request.
     * @return A message.
     */
    public Result buyJoker(Http.Request request) {
        User user = getUserFromSession(request);
        if (user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int jokerId = json.findPath("jokerId").intValue();
        int oldAmountOfJokers = jokers.getJokerAmountOfUser(jokerId, user.getId());
        int coins = user.getCoins();
        int price = jokers.getJokerById(jokerId, user.getId()).getPrice();
        System.out.println("JokerId: " + jokerId);
        ObjectNode result = Json.newObject();
        if (coins >= price) {
            int newAmountOfJokers = oldAmountOfJokers + 1;
            int newAmountOfCoins = coins - price;
            jokers.setJokerAmountOfUser(jokerId, user.getId(), newAmountOfJokers);
            user.setCoins(newAmountOfCoins);
            user.save();
            result.put("success", true);
            result.put("newAmountOfJokers", newAmountOfJokers);
            result.put("newAmountOfCoins", newAmountOfCoins);
        } else {
            result.put("not enough coins available", false);
        }
        return ok(result);
    }

    /**
     * This method is used to use a joker.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then uses the joker and returns a message.
     *
     * @param request The HTTP request.
     * @return A message.
     */
    public Result useJoker(Http.Request request) {
        User user = getUserFromSession(request);
        ObjectNode result = Json.newObject();
        if (user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int jokerId = json.findPath("jokerId").intValue();
        int amountOfJokers = jokers.getJokerAmountOfUser(jokerId, user.getId());
        if (amountOfJokers >= 1) {
            int newAmountOfJokers = amountOfJokers - 1;
            jokers.setJokerAmountOfUser(jokerId, user.getId(), newAmountOfJokers);

            result.put("success", true);
            result.put("newAmountOfJokers", newAmountOfJokers);
        } else {
            result.put("not enough Jokers available", false);
        }
        return ok(result);
    }

    /**
     * This method is used to get the user from the session.
     *
     * @param request The HTTP request.
     * @return The user.
     */
    private User getUserFromSession(Http.Request request) {
        return request
                .session()
                .get("userID")
                .map(Integer::parseInt)
                .map(users::getUserById)
                .orElse(null);
    }
}
