package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import controllers.interfaces.AbstractQuizFactory;
import controllers.interfaces.AbstractUserFactory;
import models.HighscoreFactory;
import models.JokerFactory;
import play.libs.Json;
import play.mvc.*;

import controllers.interfaces.*;
import models.UserFactory;
import models.QuizFactory;

import java.util.List;
import java.util.Map;


public class QuizController extends Controller {

    private Quiz quiz;
    private final AbstractQuizFactory quizzes;
    private final AbstractUserFactory users;
    private final AbstractHighscoreFactory scores;
    private final JokerGetter jokerGetter;

    @Inject
    public QuizController(QuizFactory quizzes, UserFactory users, HighscoreFactory scores, JokerFactory jokers) {
        this.quizzes = quizzes;
        this.users = users;
        this.scores = scores;
        this.jokerGetter = jokers;
    }

    public Result quiz(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        Map<Integer, String> possibleQuizNames = quizzes.getPossibleQuizNames();
        List<Integer> jokerAmounts = jokerGetter.getAllJokerAmountsOfUser(user.getId());
        return ok(views.html.quiz.render(user, jokerAmounts, possibleQuizNames));
    }

    public Result selectQuizAndGetName(Http.Request request){
        JsonNode json = request.body().asJson();
        int quizId = json.findPath("quizId").asInt();
        quiz = quizzes.getQuizById(quizId);

        String quizName = quiz.getName();
        return ok(Json.newObject().put("quizName", quizName));
    }

    public void setQuiz(int quizId){
        quiz = quizzes.getQuizById(quizId);
    }

    public Result getNextQuestion() {
        ObjectNode result = Json.newObject();

        if (quiz.hasNextQuestion()) {
            quiz.nextQuestion();
            QuizQuestion question = quiz.getCurrentQuestion();
            result.put("question", question.getQuestionText());
            result.set("answers", Json.toJson(question.getAnswers()));
        } else {
            result.put("endOfQuiz", true);
        }

        return ok(result);
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

    public Result savePointsIfHighscore(Http.Request request) {
        User user = getUserFromSession(request);
        if(user == null) return redirect(routes.LoginController.login());

        JsonNode json = request.body().asJson();
        int newHighscore = json.findPath("score").asInt();
        System.out.println(newHighscore);
        Highscore oldHighscore = scores.getHighscoreOfUserAndQuiz(user.getId(), quiz.getId());

        if(oldHighscore == null || newHighscore > oldHighscore.getScore()){
            scores.createHighscore(user.getId(), quiz.getId(), newHighscore);
        }

        return ok();
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
