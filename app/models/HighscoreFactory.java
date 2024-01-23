package models;

import controllers.interfaces.ScoreboardInterface;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class HighscoreFactory implements ScoreboardInterface {
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
                Highscore highscore = new Highscore(rs);
                highscores.add(highscore);
            }
            stmt.close();
            return highscores;
        });
    }

    @Override
    public List<Highscore> getHighscoresOfUser(int userId) {
        return db.withConnection(conn -> {
            List<Highscore> highscores = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores WHERE user_idUser = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Highscore highscore = new Highscore(rs);
                highscores.add(highscore);
            }
            stmt.close();
            return highscores;
        });
    }

    public class Highscore implements Comparable<Highscore> {
        private final int score;
        private final int quizId;
        private final int userId;

        private Highscore(ResultSet rs) throws SQLException {
            this.score = rs.getInt("highscore");
            this.quizId = rs.getInt("quiz_idQuiz");
            this.userId = rs.getInt("user_iduser");
        }

        public int getQuizId() {
            return quizId;
        }

        public int getUserId() {
            return userId;
        }

        public int getScore() {
            return score;
        }

        public String getUserName(){
            return users.getUserById(userId).getName();
        }

        public String getQuizName() {
            return quizes.getQuizById(quizId).getName();
        }

        @Override
        public int compareTo(Highscore that) {
            return that.score - this.score;
        }
    }
}
