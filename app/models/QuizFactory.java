package models;

import controllers.interfaces.QuizInterface;
import play.db.Database;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class QuizFactory {
    private Database db;
    @Inject
    QuizFactory(Database db) {
        this.db = db;
    }
    public Quiz getQuiz(String name){
        return db.withConnection(conn -> {
            UserFactory.User user = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quiz WHERE idQser = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new UserFactory.User(rs);
            }
            stmt.close();
            return user;
        });
    }
    public List<String> getPossibleQuizes(){
        List<String> possibleQuizes = new ArrayList<>();
        possibleQuizes.add("FirstQuiz");
        possibleQuizes.add("SecondQuiz");
        possibleQuizes.add("ThirdQuiz");

        return possibleQuizes;
    }

    public static class Quiz implements QuizInterface {
        private static DummyQuizInterface INSTANCE;
        private Queue<QuizQuestion> questions;
        private QuizFactory.QuizQuestion currentQuestion;

        @Override
        public QuizFactory.QuizQuestion getCurrentQuestion() {
            return currentQuestion;
        }

        @Override
        public void nextQuestion() {
            currentQuestion = questions.poll();
        }

        @Override
        public boolean hasNextQuestion() {
            return !questions.isEmpty();
        }

        @Override
        public boolean isCorrectAnswer(String answer) {
            return currentQuestion.isCorrectAnswer(answer);
        }

        @Override
        public String getCorrectAnswer() {
            return currentQuestion.getCorrectAnswer();
        }

        @Override
        public void resetQuiz() {}

        @Override
        public void setStartingQuestion(int questionIndex) {
            resetQuiz();
        }
    }

    public static class QuizQuestion {
        private final String questionText;
        private final List<String> answers;
        private final String correctAnswer;

        public QuizQuestion(String questionText, List<String> answers, String correctAnswer) {
            this.questionText = questionText;
            this.answers = answers;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestionText() {
            return questionText;
        }

        public List<String> getAnswers() {
            return answers;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public boolean isCorrectAnswer(String answer){
            return answer.equals(correctAnswer);
        }
    }
}
