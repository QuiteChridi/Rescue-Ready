package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Files.TemporaryFile;

import java.nio.file.Paths;

import com.google.inject.Inject;
import controllers.interfaces.*;
import models.*;

import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.util.*;


public class ProfileController extends Controller {

    private final AbstractUserFactory users;
    private final AbstractHighscoreFactory scores;
    private final AbstractQuizFactory quizzes;
    private controllers.interfaces.User currentUser;

    @Inject
    public ProfileController(UserFactory users, HighscoreFactory scores, QuizFactory quizzes) {
        this.users = users;
        this.scores = scores;
        this.quizzes = quizzes;
    }

    public Result profile(Http.Request request) {
        try {
            int userId = Integer.parseInt(request.session().get("userID").orElse(null));

            List<controllers.interfaces.Highscore> highscores = scores.getHighscoresOfUser(userId);
            highscores.sort(controllers.interfaces.Highscore::compareTo);

            return ok(profile.render(users.getUserById(userId), highscores));

        } catch (NumberFormatException e) {
            return redirect(routes.LoginController.login());
        }
    }

    public Result signup() {
        return ok(signup.render());
    }

    public Result friendProfile(int friendUserId) {
        controllers.interfaces.User friend = users.getUserById(friendUserId);

        List<controllers.interfaces.Highscore> highscores = scores.getHighscoresOfUser(friendUserId);
        highscores.sort(controllers.interfaces.Highscore::compareTo);

        if (friend != null) {
            return ok(friendProfile.render(friend, highscores));
        } else {
            return notFound("Friend not found");
        }
    }

    public Result friends(Http.Request request) {
        String userIDString = request.session().get("userID").orElse(null);

        if (userIDString == null || userIDString.equals("leer")) {
            return redirect(routes.LoginController.login());
        }

        try {
            int userID = Integer.parseInt(userIDString);
            controllers.interfaces.User user = users.getUserById(userID);
            if (user != null) {
                return ok(friends.render(user));
            } else {
                return redirect(routes.LoginController.login());
            }
        } catch (NumberFormatException e) {
            return redirect(routes.LoginController.login());
        }
    }


    public Result saveProfilePicToAssets(Http.Request request) {
        System.out.println("SaveProfilePicToAsset wurde aufgerufen");
        controllers.interfaces.User user = getUserFromSession(request);

        if (user != null) {
            Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();

            Http.MultipartFormData.FilePart<TemporaryFile> picture = body.getFile("picture");

            String fileName = picture.getFilename();
            TemporaryFile file = picture.getRef();

            file.copyTo(Paths.get(routes.Assets._defaultPrefix(), "public/images/profilePics", fileName), true);
        } else {
            return redirect(routes.LoginController.login());
        }
        return redirect(routes.ProfileController.profile());
    }

    public Result saveChangesToUser(Http.Request request) {
        controllers.interfaces.User user = getUserFromSession(request);
        if (user != null) {
            JsonNode json = request.body().asJson();
            String newUsername = json.findPath("username").textValue();
            String newPassword = json.findPath("password").textValue();
            String newEmail = json.findPath("email").textValue();
            String newProfilePic = "images/profilePics/" + json.findPath("profilePicPath").textValue();

            if (newProfilePic.equals("images/profilePics/")) {
                newProfilePic = user.getProfilePicPath();
            }
            user.setMail(newEmail);
            user.setName(newUsername);
            user.setPassword(newPassword);
            user.setProfilePicPath(newProfilePic);
            user.save();

            ObjectNode result = Json.newObject();
            result.put("success", true);

            return ok(result);
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    private int getUserIdFromSession(Http.Request request) {
        String userIDString = request.session().get("userID").orElse(null);
        return (userIDString != null && !userIDString.equals("leer")) ? Integer.parseInt(userIDString) : -1;
    }

    private controllers.interfaces.User getUserFromSession(Http.Request request) {
        int userID = getUserIdFromSession(request);
        return (userID != -1) ? users.getUserById(userID) : null;
    }

    public Result getProfilePic(Http.Request request) {
        controllers.interfaces.User user = getUserFromSession(request);

        if (user != null) {

            String pp = user.getProfilePicPath();
            System.out.println("Profilbild: " + pp);

            return ok(Json.toJson(pp));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result searchFriends(Http.Request request) {
        String searchQuery = request.queryString("name").orElse(null);
        if (searchQuery == null || searchQuery.isEmpty()) {
            return ok(Json.toJson(new ArrayList<>()));
        }

        List<controllers.interfaces.User> matchingUsers = users.searchUsersByName(searchQuery);
        return ok(Json.toJson(matchingUsers));
    }

    public Result addFriend(Http.Request request, int userId) {
        int currentUserId = getUserIdFromSession(request);
        if (userId < 0) {
            return redirect(routes.LoginController.login());
        }
        boolean success = users.addFriend(currentUserId, userId);
        if (success) {
            return ok("Freund hinzugefügt");
        } else {
            return internalServerError("Fehler beim Hinzufügen des Freundes.");
        }
    }

    public Result removeFriend(Http.Request request, int friendId) {
        int userId = getUserIdFromSession(request);
        if (userId < 0) {
            return redirect(routes.LoginController.login());
        }
        boolean success = users.removeFriend(userId, friendId);
        if (success) {
            return ok("Freund entfernt");
        } else {
            return internalServerError("Freund konnte nicht entfernt werden");
        }
    }

    public Result searchUsers(Http.Request request) {
        String name = request.queryString("name").orElse(null);
        if (name == null || name.isEmpty()) {
            return ok(Json.toJson(new ArrayList<>()));
        }

        List<User> matchingUsers = users.searchUsersByName(name);
        return ok(Json.toJson(matchingUsers));
    }


}