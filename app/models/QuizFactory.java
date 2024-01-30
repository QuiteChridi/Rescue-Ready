package models;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import controllers.interfaces.QuizInterface;
import play.db.Database;

@Singleton
public class QuizFactory {
    private final Database db;
    @Inject
    QuizFactory(Database db) {
        this.db = db;
    }

    public Quiz getQuizById(int id){
        return db.withConnection(conn -> {
            QuizFactory.Quiz quiz = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quiz, questions WHERE idQuiz = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                quiz = new QuizFactory.Quiz(rs);
            }
            stmt.close();
            return quiz;
        });
    }

    private Queue<QuizQuestion> getQuizQuestions(int idQuiz){
        return db.withConnection(conn -> {
            Queue<QuizQuestion> questions = new LinkedList<>();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE quiz_idQuiz = ?");
            stmt.setInt(1, idQuiz);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                QuizQuestion question = new QuizQuestion(rs);
                questions.add(question);
            }
            stmt.close();
            return questions;
        });
    }

    public Map<Integer, String> getPossibleQuizNames(){
        Map<Integer, String> quizes = new HashMap<>();
        return db.withConnection(conn -> {
            QuizFactory.Quiz quiz = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quiz");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quizes.put(rs.getInt("idQuiz"), rs.getString("name"));
            }
            stmt.close();
            return quizes;
        });
    }

    public class Quiz implements QuizInterface {
        private int id;
        private String name;
        private Queue<QuizQuestion> questions;
        private QuizFactory.QuizQuestion currentQuestion;

        private Quiz(int id, String name, Queue<QuizQuestion> questions){
            this.id = id;
            this.name = name;
            this.questions = questions;
            this.currentQuestion = questions.peek();
        }

        private Quiz(ResultSet rs) throws SQLException {
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
    }

    public class QuizQuestion {
        private final String questionText;
        private final List<String> answers;
        private final String correctAnswer;

        private QuizQuestion(ResultSet rs) throws SQLException {
            this.questionText = rs.getString("question");
            this.correctAnswer = rs.getString("correctAnswer");
            this.answers = new ArrayList<>();
            this.answers.add(this.correctAnswer);
            this.answers.add(rs.getString("wrongAnswer1"));
            this.answers.add(rs.getString("wrongAnswer2"));
            this.answers.add(rs.getString("wrongAnswer3"));
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
