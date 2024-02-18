package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import controllers.interfaces.AbstractQuizFactory;
import controllers.interfaces.AbstractUserFactory;
import play.libs.Json;
import play.mvc.*;

import controllers.interfaces.*;
import models.UserFactory;
import models.QuizFactory;

import java.util.Map;


public class QuizController extends Controller {

    private Quiz quiz;
    private final AbstractQuizFactory quizzes;
    private final AbstractUserFactory users;

    @Inject
    public QuizController(QuizFactory quizzes, UserFactory users) {
        this.quizzes = quizzes;
        this.users = users;
    }

    private int getUserIdFromSession(Http.Request request) {
        String userIDString = request.session().get("userID").orElse(null);
        return (userIDString != null && !userIDString.equals("leer")) ? Integer.parseInt(userIDString) : -1;
    }

    private User getUserFromSession(Http.Request request) {
        int userID = getUserIdFromSession(request);
        return (userID != -1) ? users.getUserById(userID) : null;
    }

    public Result quiz(Http.Request request) {
        User user = getUserFromSession(request);

        if (user != null) {

            Map<Integer, String> possibleQuizNames = quizzes.getPossibleQuizNames();
            return ok(views.html.quiz.render(user, possibleQuizNames));
        } else {
            return redirect(routes.LoginController.login());
        }
    }

    public Result selectQuizAndGetName(Http.Request request){
        JsonNode json = request.body().asJson();
        int quizId = json.findPath("quizId").asInt();
        System.out.println("test");
        quiz = quizzes.getQuizById(quizId);

        String quizName = quiz.getName();
        return ok(Json.newObject().put("quizName", quizName));
    }

    public Result getNextQuestion() {
        System.out.println("Hallo");
        System.out.println(quiz.getName());

        if (!quiz.hasNextQuestion()) {
            System.out.println("Keine weiteren Fragen vorhanden");
            ObjectNode result = Json.newObject();
            result.put("endOfQuiz", true);
            return ok(result);
        }

        quiz.nextQuestion();

        QuizQuestion question = quiz.getCurrentQuestion();
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
