package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.interfaces.Quiz;
import controllers.interfaces.Scoreboard;
import models.DummyScoreboard;
import models.DummyQuiz;
import models.QuizQuestion;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.quiz.question;

import java.util.Collections;


public class QuizController extends Controller {

    Quiz quiz = new DummyQuiz();
    Scoreboard scoreboard = new DummyScoreboard();

    public Result quiz() {
        return ok(question.render());
    }

    public Result getNextQuestion() {

        if (!quiz.hasNextQuestion()) {
            return status(404, "Keine weiteren Fragen vorhanden");
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

    public Result handleResult(Http.Request request) {
        JsonNode json = request.body().asJson();
        int highscore = json.findPath("highscore").asInt();

        String username = request.session().get("username").orElse("Guest");

        if(scoreboard.isInHighscoreList(username)){
            scoreboard.updateHighscore(username, highscore);
        } else {
            scoreboard.addHighscore(username, highscore);
        }

        int rank = scoreboard.getRank(username);
        quiz.resetQuiz();

        return ok().addingToSession(request, "highscore", String.valueOf(highscore)).addingToSession(request, "rank", String.valueOf(rank));
    }
}
