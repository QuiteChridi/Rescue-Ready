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

@Singleton
public class HighscoreFactory implements AbstractHighscoreFactory {
    private final Database db;
    private final UserFactory users;
    private final QuizFactory quizes;


    @Inject
    HighscoreFactory(Database db, UserFactory users, QuizFactory quizes) {
        this.db = db;
        this.users = users;
        this.quizes = quizes;
    }

    @Override
    public  List<Highscore> getHighscoresOfQuiz(int quizId) {
        return db.withConnection(conn -> {
            List<Highscore> highscores = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores WHERE quiz_idQuiz = ?");
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

    public List<Highscore> getHighscoresOfUser(int userId) {
        return db.withConnection(conn -> {
            List<Highscore> highscores = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores WHERE user_idUser = ?");
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
        private final UserFactory.UserImplementation user;
        private final QuizFactory.QuizImplementation quiz;

        private HighscoreImplementation(ResultSet rs) throws SQLException {
            this.score = rs.getInt("highscore");
            this.quizId = rs.getInt("quiz_idQuiz");
            this.userId = rs.getInt("user_iduser");

            this.user = users.getUserById(userId);
            this.quiz = quizes.getQuizById(quizId);
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
            return user.getProfilePicPath();
        }

        @Override
        public String getUserName(){
            return user.getName();
        }

        @Override
        public String getQuizName() {
            return quiz.getName();
        }

        @Override
        public int compareTo(Highscore that) {
            return that.getScore() - this.score;
        }
    }
}
