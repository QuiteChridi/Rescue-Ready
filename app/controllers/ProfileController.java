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


public class ProfileController extends Controller {

    private final AbstractUserFactory users;
    private final AbstractHighscoreFactory scores;

    @Inject
    public ProfileController(UserFactory users, HighscoreFactory scores) {
        this.users = users;
        this.scores = scores;
    }


    public Result profile(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        List<Highscore> highscores = scores.getHighscoresOfUser(user.getId());
        highscores.sort(Highscore::compareTo);

        return ok(profile.render(user, highscores));
    }

    public Result friendProfile(int friendUserId) {
        User friend = users.getUserById(friendUserId);
        if(friend == null) return notFound("Friend not found");

        List<Highscore> highscores = scores.getHighscoresOfUser(friend.getId());
        highscores.sort(Highscore::compareTo);

        return ok(friendProfile.render(friend, highscores));
    }

    public Result saveProfilePicToAssets(Http.Request request){
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> picture = body.getFile("picture");

        String fileName = picture.getFilename();
        TemporaryFile file = picture.getRef();

        file.copyTo(Paths.get(routes.Assets._defaultPrefix(), "public/images/profilePics", fileName), true);

        return redirect(routes.ProfileController.profile());
    }

    public Result saveChangesToUser(Http.Request request) {
        String DEFAULT_PROFILE_PIC_PATH = "images/profilePics/";

        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

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


    public Result getProfilePic(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        String profilePicPath = user.getProfilePicPath();
        return ok(Json.toJson(profilePicPath));
    }

    private User getUserFromSession(Http.Request request){
        return request
                .session()
                .get("userID")
                .map(Integer::parseInt)
                .map(users::getUserById)
                .orElse(null);
    }
}
