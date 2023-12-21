package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Database;
import models.DummyDatabase;
import models.QuizQuestion;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.quiz.question;

import java.util.Collections;


public class QuizController extends Controller {
    Database database = new DummyDatabase();

    public Result quiz() {
        return ok(question.render("", Collections.emptyList(), 0));
    }

    public Result getNextQuestion() {
        QuizQuestion nextQuestion = database.getNextQuestion();

        if (nextQuestion == null) {
            return status(404, "Keine weiteren Fragen vorhanden");
        }

        JsonNode jsonQuestion = Json.newObject()
                .put("question", nextQuestion.getQuestionText())
                .set("answers", Json.toJson(nextQuestion.getAnswers()));

        return ok(jsonQuestion);
    }

    public Result checkAnswer(Http.Request request) {
        JsonNode json = request.body().asJson();
        String selectedAnswer = json.findPath("selectedAnswer").asText();

        QuizQuestion currentQuestion = database.getCurrentQuestion(); // Annahme, dass es eine Methode wie getCurrentQuestion gibt
        String correctAnswer = currentQuestion.getCorrectAnswer();

        boolean isCorrect = selectedAnswer.equals(correctAnswer);

        return ok(Json.newObject().put("isCorrect", isCorrect).put("correctAnswer", correctAnswer));
    }

    public Result handleResult(Http.Request request) {
        JsonNode json = request.body().asJson();
        int highscore = json.findPath("highscore").asInt();

        String username = request.session().get("username").orElse("Guest");

        if(database.isInList(username)){
            database.updateHighscore(username, highscore);
        } else {
            database.addHighscore(username, highscore);
        }

        int rank = database.getRank(username);

        return ok().addingToSession(request, "highscore", String.valueOf(highscore)).addingToSession(request, "rank", String.valueOf(rank));
    }
}
