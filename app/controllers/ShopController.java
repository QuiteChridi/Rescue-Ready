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

public class ShopController extends Controller {

    private final AbstractUserFactory users;

    @Inject
    public ShopController(UserFactory users) {
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

    private User getUserFromSession(Http.Request request) {
        return request
                .session()
                .get("userID")
                .map(Integer::parseInt)
                .map(users::getUserById)
                .orElse(null);
    }
}
