package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Files.TemporaryFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;


import com.google.inject.Inject;
import controllers.interfaces.*;
import models.*;

import play.api.libs.Files;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileController extends Controller {

    private final AbstractUserFactory users;
    private final AbstractHighscoreFactory scores;
    private final AbstractQuizFactory quizzes;
    private User currentUser;

    @Inject
    public ProfileController(UserFactory users, HighscoreFactory scores, QuizFactory quizzes) {
        this.users = users;
        this.scores = scores;
        this.quizzes = quizzes;
    }

    public Result profile(Http.Request request) {
        try {
            int userId = Integer.parseInt(request.session().get("userID").orElse(null));

            return ok(profile.render(users.getUserById(userId), scores.getHighscoresOfUser(userId)));

        } catch (NumberFormatException e) {
            return redirect(routes.LoginController.login());
        }
    }

    public Result signup() {
        return ok(signup.render());
    }

    public Result friendProfile(int friendUserId) {
        User friend = users.getUserById(friendUserId);

        if (friend != null) {
            Map<String, Integer> quizHighscores = getQuizHighscoresForUser(friend);
            return ok(friendProfile.render(friend, quizHighscores));
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
            User user = users.getUserById(userID);
            if (user != null) {
                return ok(friends.render(user));
            } else {
                return redirect(routes.LoginController.login());
            }
        } catch (NumberFormatException e) {
            return redirect(routes.LoginController.login());
        }
    }
    private Map<String, Integer> getQuizHighscoresForUser(User user) {
        Map<String, Integer> quizHighscores = new HashMap<>();
        List<Highscore> highscores = this.scores.getHighscoresOfUser(user.getId());

        for (Highscore highscore : highscores) {
            String quizName = quizzes.getQuizById(highscore.getQuizId()).getName();
            quizHighscores.put(quizName, highscore.getScore());
        }

        return quizHighscores;
    }

    public Result saveChangesToProfilePic(Http.Request request){
        User user = getUserFromSession(request);

        if (user != null) {
            Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();

            Http.MultipartFormData.FilePart<TemporaryFile> picture = body.getFile("picture");
            if (picture != null) {
                String fileName = picture.getFilename();
                TemporaryFile file = picture.getRef();

                try {
                    Path targetDirectory = Paths.get(routes.Assets._defaultPrefix(), "public/images/profilePics");

                    InputStream inputStream = new FileInputStream(file.path().toFile());
                    OutputStream outputStream = new FileOutputStream(targetDirectory.resolve(fileName).toFile());

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    inputStream.close();
                    outputStream.close();

                    return redirect(routes.ProfileController.profile());
                } catch (Exception e) {
                    System.err.println("Fehler beim Speichern des Profilbildes: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.err.println("Profilbild nicht gefunden");
            }
        } else {
            return redirect(routes.LoginController.login());
        }
        return redirect(routes.ProfileController.profile());
    }

    public Result saveChangesToUser(Http.Request request) {
        System.out.println("Fehler");
        User user = getUserFromSession(request);
        if (user != null) {
            JsonNode json = request.body().asJson();
            String newUsername = json.findPath("username").textValue();
            String newPassword = json.findPath("password").textValue();
            String newEmail = json.findPath("email").textValue();
            String newProfilePic = "images/profilePics/" + json.findPath("profilePicPath").textValue();

            user.setMail(newEmail);
            user.setName(newUsername);
            user.setPassword(newPassword);
            user.setProfilePicPath(newProfilePic);

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

    private User getUserFromSession(Http.Request request) {
        int userID = getUserIdFromSession(request);
        return (userID != -1) ? users.getUserById(userID) : null;
    }

    public Result getProfilePic(Http.Request request) {
        User user = getUserFromSession(request);

        if (user != null) {

            String pp = user.getProfilePicPath();
            System.out.println("Profilbild: " + pp);

            return ok(Json.toJson(pp));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result getUserData(Http.Request request) {
        User user = getUserFromSession(request);

        if (user != null) {

            String profilePicPath = user.getProfilePicPath();
            String userName = user.getName();

            ObjectNode userData = JsonNodeFactory.instance.objectNode();
            userData.put("profilePicPath", profilePicPath);
            userData.put("userName", userName);

            return ok(userData);
        } else {
            return redirect(routes.LoginController.login());
        }
    }
}