package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.interfaces.QuizInterface;
import models.UserFactory;
import models.QuizFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.quiz.quizSelection;
import views.html.quiz.quizView;


public class QuizController extends Controller {

    private QuizInterface quiz;
    private final QuizFactory quizes;
    UserFactory users;

    @Inject
    public QuizController(QuizFactory quizes, UserFactory users) {
        this.quizes = quizes;
        this.users = users;
    }

    private int getUserIdFromSession(Http.Request request) {
        String userIDString = request.session().get("userID").orElse(null);
        return (userIDString != null && !userIDString.equals("leer")) ? Integer.parseInt(userIDString) : -1;
    }

    private UserFactory.User getUserFromSession(Http.Request request) {
        int userID = getUserIdFromSession(request);
        return (userID != -1) ? users.getUserById(userID) : null;
    }

    public Result quizSelection() {
        return ok(quizSelection.render(quizes.getPossibleQuizNames()));
    }

    public Result selectQuiz(Http.Request request){
        JsonNode json = request.body().asJson();
        int quizId = json.findPath("quizId").asInt();
        System.out.println("test");
        quiz = quizes.getQuizById(quizId);
        return ok();
    }

    public Result quizView(Http.Request request) {
            UserFactory.User user = getUserFromSession(request);

            if (user != null) {
                return ok(quizView.render(user));
            } else {
                return redirect(routes.LoginController.login());
            }
    }

    public Result getNextQuestion() {
        System.out.println("Hallo");
        System.out.println(quiz);

        if (!quiz.hasNextQuestion()) {
            System.out.println("Keine weiteren Fragen vorhanden");
            ObjectNode result = Json.newObject();
            result.put("endOfQuiz", true);
            return ok(result);
        }

        quiz.nextQuestion();

        QuizFactory.QuizQuestion question = quiz.getCurrentQuestion();
        JsonNode jsonQuestion = Json.newObject()
                .put("question", question.getQuestionText())
                .set("answers", Json.toJson(question.getAnswers()));

        return ok(jsonQuestion);
    }

    public Result checkAnswer(Http.Request request) {
        JsonNode json = request.body().asJson();
        String selectedAnswer = json.findPath("selectedAnswer").asText();

        boolean isCorrect = quiz.isCorrectAnswer(selectedAnswer);
        String correctAnswer = quiz.getCorrectAnswer();

        return ok(Json.newObject().put("isCorrect", isCorrect).put("correctAnswer", correctAnswer));
    }

    public Result getCorrectAnswer() {
        String correctAnswer = quiz.getCorrectAnswer();
        return ok(Json.newObject().put("correctAnswer", correctAnswer));
    }

    public Result getFiftyFiftyJoker(Http.Request request) {
        UserFactory.User user = getUserFromSession(request);

        if (user != null) {
            int availableFiftyFiftyJoker = user.getFiftyFiftyJoker();
            return ok(Json.newObject().put("availableFiftyFiftyJoker", availableFiftyFiftyJoker));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result setFiftyFiftyJoker(Http.Request request) {
        UserFactory.User user = getUserFromSession(request);

        if (user != null) {
            JsonNode json = request.body().asJson();
            int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();
            user.setFiftyFiftyJoker(newAmountOfJokers);

            ObjectNode result = Json.newObject();
            result.put("success", true);
            result.put("newAmountOfJokers", newAmountOfJokers);
            return ok(result);
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result getPauseJoker(Http.Request request) {
        UserFactory.User user = getUserFromSession(request);

        if (user != null) {
            int availablePauseJoker = user.getPauseJoker();
            return ok(Json.newObject().put("availablePauseJoker", availablePauseJoker));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result setPauseJoker(Http.Request request) {
        UserFactory.User user = getUserFromSession(request);

        if (user != null) {
            JsonNode json = request.body().asJson();
            int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();
            user.setPauseJoker(newAmountOfJokers);

            ObjectNode result = Json.newObject();
            result.put("success", true);
            result.put("newAmountOfJokers", newAmountOfJokers);
            return ok(result);
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result getDoublePointsJoker(Http.Request request) {
        UserFactory.User user = getUserFromSession(request);

        if (user != null) {
            int availableDoublePointsJoker = user.getDoublePointsJoker();
            return ok(Json.newObject().put("availableDoublePointsJoker", availableDoublePointsJoker));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result setDoublePointsJoker(Http.Request request) {
        UserFactory.User user = getUserFromSession(request);

        if (user != null) {
            JsonNode json = request.body().asJson();
            int newAmountOfJokers = json.findPath("newAmountOfJokers").intValue();
            user.setDoublePointsJoker(newAmountOfJokers);

            ObjectNode result = Json.newObject();
            result.put("success", true);
            result.put("newAmountOfJokers", newAmountOfJokers);
            return ok(result);
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result handleResult(Http.Request request) {
        /*
        JsonNode json = request.body().asJson();
        int highscore = json.findPath("highscore").asInt();

        int userID = Integer.parseInt(request.session().get("userId").get());
        int quizId= quiz.getId();

        if(scoreboard.isInHighscoreList(quiz)){
            scoreboard.updateHighscore(username, highscore);
        } else {
            scoreboard.addHighscore(username, highscore);
        }

        int rank = scoreboard.getRank(username);*/

        return ok();
    }

}
