package controllers;

import com.google.inject.Inject;
import controllers.interfaces.AbstractHighscoreFactory;
import controllers.interfaces.AbstractUserFactory;
import models.*;

import play.mvc.*;
import views.html.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeController extends Controller {

    private final AbstractUserFactory users;
    private final AbstractHighscoreFactory scoreboard;
    private final QuizFactory quizFactory;

    @Inject
    public HomeController(UserFactory users, HighscoreFactory scoreboard, QuizFactory quizFactory) {
        this.users = users;
        this.scoreboard = scoreboard;
        this.quizFactory = quizFactory;
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
            Map<String, Integer> quizHighscores = getQuizHighscoresForUser(friend);
            return ok(friendProfile.render(friend, quizHighscores));
        } else {
            return notFound("Friend not found");
        }
    }

    public Result friends() {
        return ok(friends.render());
    }
    private Map<String, Integer> getQuizHighscoresForUser(UserFactory.User user) {
        Map<String, Integer> quizHighscores = new HashMap<>();
        List<HighscoreFactory.Highscore> highscores = this.scoreboard.getHighscoresOfUser(user.getId());

        for (HighscoreFactory.Highscore highscore : highscores) {
            String quizName = quizFactory.getQuizById(highscore.getQuizId()).getName();
            quizHighscores.put(quizName, highscore.getScore());
        }

        return quizHighscores;
    }


}