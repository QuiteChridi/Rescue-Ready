package models;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.interfaces.AbstractHighscoreFactory;
import controllers.interfaces.Highscore;
import play.db.Database;

/**
 * HighscoreFactory is a class that implements the AbstractHighscoreFactory interface.
 * It is responsible for creating Highscore objects and getting highscores from the database.
 * It is a singleton class, meaning that only one instance of it will be created.
 */
@Singleton
public class HighscoreFactory implements AbstractHighscoreFactory {

    private final Database db;

    /**
     * Constructor for HighscoreFactory.
     *
     * @param db The database to be used.
     */
    @Inject
    HighscoreFactory(Database db) {
        this.db = db;
    }

    /**
     * Method to get the highscores of a quiz.
     *
     * @param quizId The id of the quiz.
     * @return A list of highscores.
     */
    @Override
    public List<Highscore> getHighscoresOfQuiz(int quizId) {
        return db.withConnection(conn -> {
            List<Highscore> highscores = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores JOIN user ON user_iduser = user.idUser JOIN quiz ON quiz_idQuiz = quiz.idQuiz WHERE quiz_idQuiz = ?");
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Highscore highscore = new HighscoreImplementation(rs);
                highscores.add(highscore);
            }
            stmt.close();
            return highscores;
        });
    }

    /**
     * Method to get possible quiz names.
     *
     * @return A map of quiz ids and quiz names.
     */
    @Override
    public Map<Integer, String> getPossibleQuizNames() {
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
     * Method to get the highscore of a user and a quiz.
     *
     * @param userId The id of the user.
     * @param quizId The id of the quiz.
     * @return The highscore of the user and the quiz.
     */
    @Override
    public Highscore getHighscoreOfUserAndQuiz(int userId, int quizId) {
        return db.withConnection(conn -> {
            Highscore highscore = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores JOIN user ON user_iduser = user.idUser JOIN quiz ON quiz_idQuiz = quiz.idQuiz WHERE user_iduser = ? AND quiz_idQuiz = ?");
            stmt.setInt(1, userId);
            stmt.setInt(2, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                highscore = new HighscoreFactory.HighscoreImplementation(rs);
            }
            stmt.close();
            return highscore;
        });
    }

    /**
     * Method to create a highscore.
     * If a highscore already exists, it will be replaced.
     *
     * @param userId The id of the user.
     * @param quizID The id of the quiz.
     * @param score The score of the user.
     */
    @Override
    public void createHighscore(int userId, int quizID, int score) {
        db.withConnection(conn -> {
            PreparedStatement stmt = conn.prepareStatement("REPLACE INTO highscores VALUES (?,?,?)");
            stmt.setInt(1, quizID);
            stmt.setInt(2, userId);
            stmt.setInt(3, score);

            stmt.executeUpdate();
            stmt.close();
        });
    }

    /**
     * Method to get the highscores of a user.
     *
     * @param userId The id of the user.
     * @return A list of highscores.
     */
    public List<Highscore> getHighscoresOfUser(int userId) {
        return db.withConnection(conn -> {
            List<Highscore> highscores = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores JOIN user ON user_iduser = user.idUser JOIN quiz ON quiz_idQuiz = quiz.idQuiz WHERE user_iduser = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Highscore highscore = new HighscoreFactory.HighscoreImplementation(rs);
                highscores.add(highscore);
            }
            stmt.close();
            return highscores;
        });
    }

    /**
     * A static class that implements the Highscore interface.
     * It is used to create Highscore objects.
     * It is a static class because it does not need to access any fields of the HighscoreFactory class.
     */

    public static class HighscoreImplementation extends Highscore {
        private final int score;
        private final int quizId;
        private final int userId;
        private final String quizName;
        private final String userName;
        private final String profilePicPath;

        /**
         * Constructor for HighscoreImplementation.
         *
         * @param rs The result set from the database.
         * @throws SQLException If an error occurs while accessing the database.
         */
        private HighscoreImplementation(ResultSet rs) throws SQLException {
            this.score = rs.getInt("highscore");
            this.quizId = rs.getInt("quiz_idQuiz");
            this.userId = rs.getInt("user_iduser");
            this.quizName = rs.getString("quiz.name");
            this.userName = rs.getString("user.name");
            this.profilePicPath = rs.getString("user.profile_pic_path");
        }

        /**
         * Constructor for HighscoreImplementation.
         *
         * @param score The score of the user.
         * @param quizId The id of the quiz.
         * @param userId The id of the user.
         * @param quizName The name of the quiz.
         * @param userName The name of the user.
         * @param profilePicPath The path to the profile picture of the user.
         */
        public HighscoreImplementation(int score, int quizId, int userId, String quizName, String userName, String profilePicPath) {
            this.score = score;
            this.quizId = quizId;
            this.userId = userId;
            this.quizName = quizName;
            this.userName = userName;
            this.profilePicPath = profilePicPath;
        }

        /**
         * Method to get the id of the quiz.
         *
         * @return The id of the quiz.
         */
        @Override
        public int getQuizId() {
            return quizId;
        }

        /**
         * Method to get the id of the user.
         *
         * @return The id of the user.
         */
        @Override
        public int getUserId() {
            return userId;
        }

        /**
         * Method to get the score of the user.
         *
         * @return The score of the user.
         */
        @Override
        public int getScore() {
            return score;
        }

        /**
         * Method to get the path to the profile picture of the user.
         *
         * @return The path to the profile picture of the user.
         */
        @Override
        public String getProfilePicPath() {
            return profilePicPath;
        }

        /**
         * Method to get the name of the user.
         *
         * @return The name of the user.
         */
        @Override
        public String getUserName() {
            return userName;
        }

        /**
         * Method to get the name of the quiz.
         *
         * @return The name of the quiz.
         */
        @Override
        public String getQuizName() {
            return quizName;
        }

        /**
         * Method to compare two Highscore objects.
         *
         * @param that The Highscore object to compare to.
         * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
         */
        @Override
        public int compareTo(Highscore that) {
            return that.getScore() - this.score;
        }
    }
}
