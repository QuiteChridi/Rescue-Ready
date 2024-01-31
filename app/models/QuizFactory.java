package models;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import controllers.interfaces.AbstractQuizFactory;
import controllers.interfaces.Quiz;
import controllers.interfaces.QuizQuestion;
import play.db.Database;

@Singleton
public class QuizFactory implements AbstractQuizFactory {
    private final Database db;
    @Inject
    QuizFactory(Database db) {
        this.db = db;
    }

    @Override
    public QuizImplementation getQuizById(int id){
        return db.withConnection(conn -> {
            QuizImplementation quiz = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quiz, questions WHERE idQuiz = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                quiz = new QuizImplementation(rs);
            }
            stmt.close();
            return quiz;
        });
    }

    private Queue<QuizQuestionImplementation> getQuizQuestions(int idQuiz){
        return db.withConnection(conn -> {
            Queue<QuizQuestionImplementation> questions = new LinkedList<>();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE quiz_idQuiz = ?");
            stmt.setInt(1, idQuiz);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                QuizQuestionImplementation question = new QuizQuestionImplementation(rs);
                questions.add(question);
            }
            stmt.close();
            return questions;
        });
    }

    @Override
    public Map<Integer, String> getPossibleQuizNames(){
        Map<Integer, String> quizes = new HashMap<>();
        return db.withConnection(conn -> {
            QuizImplementation quiz = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quiz");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quizes.put(rs.getInt("idQuiz"), rs.getString("name"));
            }
            stmt.close();
            return quizes;
        });
    }

    public class QuizImplementation extends Quiz {
        private int id;
        private String name;
        private Queue<QuizQuestionImplementation> questions;
        private QuizQuestionImplementation currentQuestion;

        private QuizImplementation(int id, String name, Queue<QuizQuestionImplementation> questions){
            this.id = id;
            this.name = name;
            this.questions = questions;
            this.currentQuestion = questions.peek();
        }

        private QuizImplementation(ResultSet rs) throws SQLException {
            this.id = rs.getInt("idQuiz");
            this.name = rs.getString("name");
            this.questions = getQuizQuestions(this.id);
            this.currentQuestion = questions.peek();
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public QuizQuestionImplementation getCurrentQuestion() {
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
    }

    public class QuizQuestionImplementation extends QuizQuestion {
        private final String questionText;
        private final List<String> answers;
        private final String correctAnswer;

        private QuizQuestionImplementation(ResultSet rs) throws SQLException {
            this.questionText = rs.getString("question");
            this.correctAnswer = rs.getString("correctAnswer");
            this.answers = new ArrayList<>();
            this.answers.add(this.correctAnswer);
            this.answers.add(rs.getString("wrongAnswer1"));
            this.answers.add(rs.getString("wrongAnswer2"));
            this.answers.add(rs.getString("wrongAnswer3"));
        }

        @Override
        public String getQuestionText() {
            return questionText;
        }

        @Override
        public List<String> getAnswers() {
            return answers;
        }

        @Override
        public String getCorrectAnswer() {
            return correctAnswer;
        }

        @Override
        public boolean isCorrectAnswer(String answer){
            return answer.equals(correctAnswer);
        }
    }
}
