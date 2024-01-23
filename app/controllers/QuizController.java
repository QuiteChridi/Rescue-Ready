package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.interfaces.QuizInterface;
import models.HighscoreFactory;
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

    @Inject
    public QuizController(QuizFactory quizes, UserFactory users, HighscoreFactory scoreboard) {
        this.quizes = quizes;
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

    public Result quizView() {
        return ok(quizView.render());
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
