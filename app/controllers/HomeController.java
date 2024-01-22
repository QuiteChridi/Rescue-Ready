package controllers;

import models.*;
import play.mvc.*;
import views.html.*;


public class HomeController extends Controller {
    public Result main() {
        return ok(main.render());
    }

    public Result profile() {
        return ok(profile.render(DummyFriendList.getUserList()));
    }

    public Result signup() {
        return ok(signup.render());
    }

    public Result friendProfile(String name) {
        String friendName = name;
        return ok(friendProfile.render(friendName));
    }
}