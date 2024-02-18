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
import java.util.NoSuchElementException;
import java.util.Optional;

public class FriendController extends Controller{
    final FriendManager friendManager;

    @Inject
    public FriendController(UserFactory users) {
        this.friendManager = users;
    }

    public Result friends(Http.Request request) {
        try {
            int userID = getUserIdFromSession(request);
            return ok(friends.render(friendManager.getFriends(userID), friendManager.getAllUsers()));
        } catch (NoSuchElementException ex) {
            return redirect(routes.LoginController.login());
        }
    }

    public Result searchUsers(Http.Request request) {
        List<User> matchingUsers = new ArrayList<>();

        Optional<String> searchQuery = request.queryString("name");
        if (searchQuery.isPresent()) {
            matchingUsers = friendManager.searchUsersByName(searchQuery.get());
        }

        return ProfileController.ok(Json.toJson(matchingUsers));
    }

    public Result addFriend(Http.Request request, int friendId) {
        try {
            int userId = getUserIdFromSession(request);
            boolean success = friendManager.addFriend(userId, friendId);
            if (success) {
                return ProfileController.ok("Freund hinzugefügt");
            } else {
                return ProfileController.internalServerError("Fehler beim Hinzufügen des Freundes.");
            }
        } catch (NoSuchElementException ex) {
            return redirect(routes.LoginController.login());
        }
    }

    public Result removeFriend(Http.Request request, int friendId) {
        try {
            int userId = getUserIdFromSession(request);
            boolean success = friendManager.removeFriend(userId, friendId);
            if (success) {
                return ProfileController.ok("Freund entfernt");
            } else {
                return ProfileController.internalServerError("Freund konnte nicht entfernt werden");
            }
        } catch (NoSuchElementException ex) {
            return redirect(routes.LoginController.login());
        }
    }

    private int getUserIdFromSession(Http.Request request) throws NoSuchElementException {
        return request
                .session()
                .get("userID")
                .map(Integer::parseInt)
                .orElseThrow();
    }
}