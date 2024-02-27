package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Files.TemporaryFile;

import java.nio.file.Paths;
import java.util.List;

import com.google.inject.Inject;
import controllers.interfaces.*;
import models.*;

import play.libs.Json;
import play.mvc.*;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's profile page.
 */
public class ProfileController extends Controller {
    private final AbstractUserFactory users;
    private final AbstractHighscoreFactory scores;

    /**
     * Constructor to inject the UserFactory and HighscoreFactory
     *
     * @param users  UserFactory
     * @param scores HighscoreFactory
     */
    @Inject
    public ProfileController(UserFactory users, HighscoreFactory scores) {
        this.users = users;
        this.scores = scores;
    }

    /**
     * Renders the profile page with the user's highscores
     *
     * @param request Http.Request with the user's session
     * @return Result with the profile page
     */
    public Result profile(Http.Request request) {
        User user = getUserFromSession(request);
        if (user == null) return redirect(routes.LoginController.login());

        List<Highscore> highscores = scores.getHighscoresOfUser(user.getId());
        highscores.sort(Highscore::compareTo);

        return ok(profile.render(user, highscores));
    }

    /**
     * Renders the friend profile page
     *
     * @param otherUserId int with the id of the friend
     * @return Result with the friend profile page
     */
    public Result friendProfile(int otherUserId) {
        User otherUser = users.getUserById(otherUserId);
        if (otherUser == null) return notFound("Friend not found");

        List<Highscore> highscores = scores.getHighscoresOfUser(otherUser.getId());
        highscores.sort(Highscore::compareTo);

        return ok(friendProfile.render(otherUser, highscores));
    }

    /**
     * Saves the profile picture to the assets folder
     *
     * @param request Http.Request with the picture to save
     * @return Result with the profile page after saving the picture
     */
    public Result saveProfilePicToAssets(Http.Request request) {
        User user = getUserFromSession(request);
        if (user == null) return redirect(routes.LoginController.login());

        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> picture = body.getFile("picture");

        String fileName = picture.getFilename();
        TemporaryFile file = picture.getRef();

        file.copyTo(Paths.get(routes.Assets._defaultPrefix(), "public/images/profilePics", fileName), true);

        return redirect(routes.ProfileController.profile());
    }

    /**
     * Saves changes to the user's profile to the database
     *
     * @param request Http.Request with the changes to save
     * @return Result with the profile page after saving the changes
     */
    public Result saveChangesToUser(Http.Request request) {
        String DEFAULT_PROFILE_PIC_PATH = "images/profilePics/";

        User user = getUserFromSession(request);
        if (user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        String newUsername = json.findPath("username").textValue();
        String newPassword = json.findPath("password").textValue();
        String newEmail = json.findPath("email").textValue();
        String newProfilePicPath = DEFAULT_PROFILE_PIC_PATH + json.findPath("profilePicPath").textValue();

        if (!newProfilePicPath.equals(DEFAULT_PROFILE_PIC_PATH)) {
            user.setProfilePicPath(newProfilePicPath);
        }
        user.setMail(newEmail);
        user.setName(newUsername);
        user.setPassword(newPassword);
        user.save();

        ObjectNode result = Json.newObject();
        result.put("success", true);

        return ok(result);
    }

    /**
     * Gets the user's profile picture path from the database and returns it as a JSON
     *
     * @param request Http.Request with the user's session
     * @return Result with the user's profile picture path
     */
    public Result getProfilePic(Http.Request request) {
        User user = getUserFromSession(request);
        if (user == null) return redirect(routes.LoginController.login());

        String profilePicPath = user.getProfilePicPath();
        return ok(Json.toJson(profilePicPath));
    }

    /**
     * Gets the user from the session and returns it as a JSON
     *
     * @param request Http.Request with the user's session
     * @return result with the user as a JSON
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
