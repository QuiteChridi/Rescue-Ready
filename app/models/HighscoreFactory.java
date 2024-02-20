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
import controllers.interfaces.Quiz;
import controllers.interfaces.User;
import play.db.Database;

@Singleton
public class HighscoreFactory implements AbstractHighscoreFactory {
    private final Database db;

    @Inject
    HighscoreFactory(Database db) {
        this.db = db;
    }

    @Override
    public  List<Highscore> getHighscoresOfQuiz(int quizId) {
        return db.withConnection(conn -> {
            List<Highscore> highscores = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores JOIN user ON user_iduser = user.iduser JOIN quiz ON quiz_idQuiz = quiz.idQuiz WHERE quiz_idQuiz = ?");
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

    @Override
    public Highscore getHighscoreOfUserAndQuiz(int userId, int quizId) {
        return db.withConnection(conn -> {
            Highscore highscore = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores JOIN user ON user_iduser = user.iduser JOIN quiz ON quiz_idQuiz = quiz.idQuiz WHERE user_iduser = ? AND quiz_idQuiz = ?");
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

    public List<Highscore> getHighscoresOfUser(int userId) {
        return db.withConnection(conn -> {
            List<Highscore> highscores = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores JOIN user ON user_iduser = user.iduser JOIN quiz ON quiz_idQuiz = quiz.idQuiz WHERE user_iduser = ?");
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

    public class HighscoreImplementation extends Highscore {
        private final int score;
        private final int quizId;
        private final int userId;
        private final String quizName;
        private final String userName;
        private final String profilePicPath;


        private HighscoreImplementation(ResultSet rs) throws SQLException {
            this.score = rs.getInt("highscore");
            this.quizId = rs.getInt("quiz_idQuiz");
            this.userId = rs.getInt("user_iduser");
            this.quizName = rs.getString("quiz.name");
            this.userName = rs.getString("user.name");
            this.profilePicPath = rs.getString("user.profile_pic_path");
        }

        public HighscoreImplementation(int score, int quizId, int userId, String quizName, String userName, String profilePicPath) {
            this.score = score;
            this.quizId = quizId;
            this.userId = userId;
            this.quizName = quizName;
            this.userName = userName;
            this.profilePicPath = profilePicPath;
        }

        @Override
        public int getQuizId() {
            return quizId;
        }

        @Override
        public int getUserId() {
            return userId;
        }

        @Override
        public int getScore() {
            return score;
        }

        @Override
        public String getProfilePicPath() {
            return profilePicPath;
        }

        @Override
        public String getUserName(){
            return userName;
        }

        @Override
        public String getQuizName() {
            return quizName;
        }

        @Override
        public int compareTo(Highscore that) {
            return that.getScore() - this.score;
        }
    }
}
