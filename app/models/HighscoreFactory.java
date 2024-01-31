package models;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controllers.interfaces.AbstractHighscoreFactory;
import controllers.interfaces.Highscore;
import models.QuizFactory;
import models.UserFactory;
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
    public List<Highscore> getHighscoresOfUser(int userId) {
        return db.withConnection(conn -> {
            List<Highscore> highscores = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM highscores WHERE user_idUser = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Highscore highscore = new HighscoreImplementation(rs);
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

        private HighscoreImplementation(ResultSet rs) throws SQLException {
            this.score = rs.getInt("highscore");
            this.quizId = rs.getInt("quiz_idQuiz");
            this.userId = rs.getInt("user_iduser");
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
        public String getUserName(){
            return users.getUserById(userId).getName();
        }

        @Override
        public String getQuizName() {
            return quizes.getQuizById(quizId).getName();
        }

        @Override
        public int compareTo(Highscore that) {
            return that.getScore() - this.score;
        }
    }
}
