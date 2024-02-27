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

/**
 * QuizFactory is a class that implements the AbstractQuizFactory interface.
 * It is responsible for creating Quiz objects and getting quizzes from the database.
 * It is a singleton class, meaning that only one instance of it will be created.
 */
@Singleton
public class QuizFactory implements AbstractQuizFactory {
    private final Database db;

    /**
     * Constructor for QuizFactory.
     *
     * @param db The database to be used.
     */
    @Inject
    QuizFactory(Database db) {
        this.db = db;
    }

    /**
     * Method to get a quiz by its id.
     *
     * @param id The id of the quiz.
     * @return The quiz.
     */
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

    /**
     * Method to get all quiz questions.
     *
     * @return A list of quizzes.
     */
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

    /**
     * Method to get possible quiz names.
     *
     * @return A map of quiz ids and quiz names.
     */
    @Override
    public Map<Integer, String> getPossibleQuizNames(){
        Map<Integer, String> quizes = new HashMap<>();
        return db.withConnection(conn -> {
            QuizFactory.QuizImplementation quiz = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quiz");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quizes.put(rs.getInt("idQuiz"), rs.getString("name"));
            }
            stmt.close();
            return quizes;
        });
    }

    /**
     * A static class that extends the Quiz class.
     * It is used to create Quiz objects.
     */
    public class QuizImplementation extends Quiz {
        private int id;
        private String name;
        private Queue<QuizQuestionImplementation> questions;
        private QuizQuestionImplementation currentQuestion;

        /**
         * Constructor for QuizImplementation. This constructor is private and can only be used by the QuizFactory class.
         *
         * @param id        The id of the quiz.
         * @param name      The name of the quiz.
         * @param questions The questions of the quiz.
         */
        private QuizImplementation(int id, String name, Queue<QuizQuestionImplementation> questions){
            this.id = id;
            this.name = name;
            this.questions = questions;
            this.currentQuestion = questions.peek();
        }

        /**
         * Constructor for QuizImplementation. This constructor is private and can only be used by the QuizFactory class.
         *
         * @param rs The result set from the database.
         */
        private QuizImplementation(ResultSet rs) throws SQLException {
            this.id = rs.getInt("idQuiz");
            this.name = rs.getString("name");
            this.questions = getQuizQuestions(this.id);
            this.currentQuestion = questions.peek();
        }

        /**
         * Method to get the id of the quiz.
         *
         * @return The id of the quiz.
         */
        public int getId() {
            return id;
        }

        /**
         * Method to get the name of the quiz.
         *
         * @return The name of the quiz.
         */
        public String getName() {
            return name;
        }

        /**
         * Method to get the current question of the quiz.
         *
         * @return The current question of the quiz.
         */
        @Override
        public QuizQuestionImplementation getCurrentQuestion() {
            return currentQuestion;
        }

        /**
         * Method to get the next question of the quiz.
         */
        @Override
        public void nextQuestion() {
            currentQuestion = questions.poll();
        }

        /**
         * Method to check if there are more questions in the quiz.
         *
         * @return True if there are more questions, false otherwise.
         */
        @Override
        public boolean hasNextQuestion() {
            return !questions.isEmpty();
        }

        /**
         * Method to check if the answer is correct.
         *
         * @param answer The answer to be checked.
         * @return True if the answer is correct, false otherwise.
         */
        @Override
        public boolean isCorrectAnswer(String answer) {
            return currentQuestion.isCorrectAnswer(answer);
        }

        /**
         * Method to get the correct answer.
         *
         * @return The correct answer.
         */
        @Override
        public String getCorrectAnswer() {
            return currentQuestion.getCorrectAnswer();
        }
    }

    /**
     * A static class that extends the QuizQuestion class.
     * It is used to create QuizQuestion objects.
     */
    public class QuizQuestionImplementation extends QuizQuestion {
        private final String questionText;
        private final List<String> answers;
        private final String correctAnswer;

        /**
         * Constructor for QuizQuestionImplementation. This constructor is private and can only be used by the QuizFactory class.
         * @param rs The result set from the database.
         * @throws SQLException If an SQL exception occurs.
         */
        private QuizQuestionImplementation(ResultSet rs) throws SQLException {
            this.questionText = rs.getString("question");
            this.correctAnswer = rs.getString("correctAnswer");
            this.answers = new ArrayList<>();
            this.answers.add(this.correctAnswer);
            this.answers.add(rs.getString("wrongAnswer1"));
            this.answers.add(rs.getString("wrongAnswer2"));
            this.answers.add(rs.getString("wrongAnswer3"));
        }

        /**
         * Method to get the question text.
         *
         * @return The question text.
         */
        @Override
        public String getQuestionText() {
            return questionText;
        }

        /**
         * Method to get the answers.
         *
         * @return The answers.
         */
        @Override
        public List<String> getAnswers() {
            return answers;
        }

        /**
         * Method to get the correct answer.
         *
         * @return The correct answer.
         */
        @Override
        public String getCorrectAnswer() {
            return correctAnswer;
        }

        /**
         * Method to check if the answer is correct.
         *
         * @param answer The answer to be checked.
         * @return True if the answer is correct, false otherwise.
         */
        @Override
        public boolean isCorrectAnswer(String answer){
            return answer.equals(correctAnswer);
        }
    }
}
