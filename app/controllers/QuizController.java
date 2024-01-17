package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.interfaces.Quiz;
import controllers.interfaces.Scoreboard;
import models.DummyScoreboard;
import models.DummyQuiz;
import models.QuizFactory;
import models.QuizQuestion;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.quiz.quizSelection;
import views.html.quiz.quizView;


public class QuizController extends Controller {

    Quiz quiz = DummyQuiz.getInstance();
    Scoreboard scoreboard = DummyScoreboard.getInstance();

    public Result quizSelection() {
        return ok(quizSelection.render(QuizFactory.getPossibleQuizes()));
    }

    public Result selectQuiz(Http.Request request){

        JsonNode json = request.body().asJson();
        String quizName = json.findPath("quizName").asText();

        quiz = QuizFactory.getQuiz(quizName);
        return ok();

    }

    public Result quizView() {
        quiz.setStartingQuestion(0);
        return ok(quizView.render());
    }

    public Result getNextQuestion() {
        System.out.println("Hallo");
        System.out.println(quiz);

        if (!quiz.hasNextQuestion()) {
            System.out.println("Hallo2");
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
