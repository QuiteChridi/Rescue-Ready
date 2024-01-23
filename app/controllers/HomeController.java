package controllers;

import akka.event.Logging;
import com.google.inject.Inject;
import models.*;
import play.mvc.*;
import views.html.*;


public class HomeController extends Controller {

    UserFactory users;

    @Inject
    public HomeController(UserFactory users) {
        this.users = users;
    }

    public Result main() {
        return ok(main.render());
    }

    public Result profile(Http.Request request) {
        String userIDString = request.session().get("userID").orElse(null);

        if (userIDString == null || userIDString.equals("leer")) {
            return redirect(routes.LoginController.login());
        }

        try {
            int userID = Integer.parseInt(userIDString);
            UserFactory.User user = users.getUserById(userID);
            if (user != null) {
                return ok(profile.render(user));
            } else {
                return redirect(routes.LoginController.login());
            }
        } catch (NumberFormatException e) {
            return redirect(routes.LoginController.login());
        }
    }

    public Result signup() {
        return ok(signup.render());
    }

    public Result friendProfile(int friendUserId) {
        UserFactory.User friend = users.getUserById(friendUserId);

        if (friend != null) {
            return ok(friendProfile.render(friend));
        } else {
            return notFound("Friend not found");
        }
    }

    public Result shop() {
        return ok(shop.render());
    }
}