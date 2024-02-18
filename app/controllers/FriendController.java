package controllers;

import com.google.inject.Inject;
import controllers.interfaces.FriendManager;
import controllers.interfaces.User;

import models.UserFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import views.html.friends;
import play.mvc.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendController extends Controller{
    final FriendManager friendManager;

    @Inject
    public FriendController(UserFactory users) {
        this.friendManager = users;
    }

    public Result friends(Http.Request request) {
        int userId = getUserIdFromSession(request);
        if(userId == 0) return redirect(routes.LoginController.login());

        return ok(friends.render(friendManager.getFriends(userId), friendManager.getAllUsers()));
    }

    public Result addFriend(Http.Request request, int friendId) {
        int userId = getUserIdFromSession(request);
        if(userId == 0) return redirect(routes.LoginController.login());

        boolean success = friendManager.addFriend(userId, friendId);
        if (success) {
            return ProfileController.ok("Freund hinzugefügt");
        } else {
            return ProfileController.internalServerError("Fehler beim Hinzufügen des Freundes.");
        }
    }

    public Result removeFriend(Http.Request request, int friendId) {
        int userId = getUserIdFromSession(request);
        if(userId == 0) return redirect(routes.LoginController.login());

        boolean success = friendManager.removeFriend(userId, friendId);
        if (success) {
            return ProfileController.ok("Freund entfernt");
        } else {
            return ProfileController.internalServerError("Freund konnte nicht entfernt werden");
        }
    }

    private int getUserIdFromSession(Http.Request request){
        return request
                .session()
                .get("userID")
                .map(Integer::parseInt)
                .orElse(0);
    }

    public Result searchUsers(Http.Request request) {
        List<User> matchingUsers = new ArrayList<>();

        Optional<String> searchQuery = request.queryString("name");
        if (searchQuery.isPresent()) {
            matchingUsers = friendManager.searchUsersByName(searchQuery.get());
        }

        return ProfileController.ok(Json.toJson(matchingUsers));
    }
}