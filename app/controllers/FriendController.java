package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
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

/**
 * This controller contains an action to handle HTTP requests
 * to the application's friend page.
 * It extends the Controller class provided by Play.
 */

public class FriendController extends Controller {

    /**
     * The friendManager is used to manage the friends of a user.
     */
    final FriendManager friendManager;

    /**
     * The constructor is used to inject the friendManager.
     *
     * @param users The friendManager to be injected.
     * @Inject is used to inject the friendManager.
     */
    @Inject
    public FriendController(UserFactory users) {
        this.friendManager = users;
    }

    /**
     * This method is used to render the friend page.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then returns the friend page.
     *
     * @param request The HTTP request.
     * @return The friend page.
     */
    public Result friends(Http.Request request) {
        int userId = getUserIdFromSession(request);
        if (userId == 0) return redirect(routes.LoginController.login());

        return ok(friends.render(friendManager.getFriends(userId), friendManager.getNotFriends(userId)));
    }

    /**
     * This method is used to add a friend to the user's friend list.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then adds the friend to the user's friend list and returns a message.
     *
     * @param request  The HTTP request.
     * @param friendId The ID of the friend to be added.
     * @return A message.
     */
    public Result addFriend(Http.Request request, int friendId) {
        int userId = getUserIdFromSession(request);
        if (userId == 0) return redirect(routes.LoginController.login());

        boolean success = friendManager.addFriend(userId, friendId);
        if (success) {
            return ProfileController.ok("Freund hinzugefügt");
        } else {
            return ProfileController.internalServerError("Fehler beim Hinzufügen des Freundes.");
        }
    }

    /**
     * This method is used to remove a friend from the user's friend list.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then removes the friend from the user's friend list and returns a message.
     *
     * @param request  The HTTP request.
     * @param friendId The ID of the friend to be removed.
     * @return A message.
     */
    public Result removeFriend(Http.Request request, int friendId) {
        int userId = getUserIdFromSession(request);
        if (userId == 0) return redirect(routes.LoginController.login());

        boolean success = friendManager.removeFriend(userId, friendId);
        if (success) {
            return ProfileController.ok("Freund entfernt");
        } else {
            return ProfileController.internalServerError("Freund konnte nicht entfernt werden");
        }
    }

    /**
     * This method is used to check if two users are friends.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then checks if the two users are friends and returns the result.
     *
     * @param request     The HTTP request.
     * @param otherUserId The ID of the other user.
     * @return The result.
     */
    public Result checkFriendship(Http.Request request, int otherUserId) {
        int userId = getUserIdFromSession(request);
        if (userId == 0) return redirect(routes.LoginController.login());

        ObjectNode result = Json.newObject();
        result.put("isFriendship", friendManager.isFriend(userId, otherUserId));
        return ok(result);
    }

    /**
     * This method is used to get the ID of the user from the session.
     *
     * @param request The HTTP request.
     * @return The ID of the user.
     */
    private int getUserIdFromSession(Http.Request request) {
        return request
                .session()
                .get("userID")
                .map(Integer::parseInt)
                .orElse(0);
    }

    /**
     * This method is used to search for users by name.
     * It checks if the user is logged in and if not redirects to the login page.
     * It then searches for users by name and returns the result.
     *
     * @param request The HTTP request.
     * @return The result.
     */
    public Result searchUsers(Http.Request request) {
        List<User> matchingUsers = new ArrayList<>();

        Optional<String> searchQuery = request.queryString("name");
        if (searchQuery.isPresent()) {
            matchingUsers = friendManager.searchUsersByName(searchQuery.get());
        }

        return ProfileController.ok(Json.toJson(matchingUsers));
    }
}