package controllers;

import com.google.inject.Inject;
import controllers.interfaces.FriendManager;
import controllers.interfaces.User;
import models.HighscoreFactory;
import models.QuizFactory;
import models.UserFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

public class FriendController {
    final FriendManager friendManager;

    @Inject
    public FriendController(UserFactory users) {
        this.friendManager = users;
    }

    public Result searchUsers(Http.Request request) {
        String searchQuery = request.queryString("name").orElse(null);
        if (searchQuery == null || searchQuery.isEmpty()) {
            return ProfileController.ok(Json.toJson(new ArrayList<Object>()));
        }

        List<User> matchingUsers = friendManager.searchUsersByName(searchQuery);
        return ProfileController.ok(Json.toJson(matchingUsers));
    }

    public Result addFriend(Http.Request request, int userId) {
        int currentUserId = getUserIdFromSession(request);
        if (userId < 0) {
            return ProfileController.redirect(routes.LoginController.login());
        }
        boolean success = friendManager.addFriend(currentUserId, userId);
        if (success) {
            return ProfileController.ok("Freund hinzugefügt");
        } else {
            return ProfileController.internalServerError("Fehler beim Hinzufügen des Freundes.");
        }
    }

    public Result removeFriend(Http.Request request, int friendId) {
        int userId = getUserIdFromSession(request);
        if (userId < 0) {
            return ProfileController.redirect(routes.LoginController.login());
        }
        boolean success = friendManager.removeFriend(userId, friendId);
        if (success) {
            return ProfileController.ok("Freund entfernt");
        } else {
            return ProfileController.internalServerError("Freund konnte nicht entfernt werden");
        }
    }

    private int getUserIdFromSession(Http.Request request) {
        return request
                .session()
                .get("userID")
                .map(Integer::parseInt)
                .orElse(0);
    }
}