package models;

import controllers.interfaces.Highscore;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolution;
import play.db.evolutions.Evolutions;
import static org.junit.Assert.*;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class HighscoreFactoryTest {
    private Database database;
    private HighscoreFactory highscores;

    private String NAME_USER = "testuser";
    private String PASSWORD_USER = "testpassword";
    private String EMAIL_USER = "test@test.de";
    private int ID_USER = 99;
    private String NAME_QUIZ = "testquiz";
    private int ID_QUIZ = 99;
    private int SCORE_HIGHSCORE = 100;
    @Before
    public void givenADatabaseWithAnUserAnQuizAndAnHighscore(){
        database = Databases.inMemory("inMemory");
        Evolutions.applyEvolutions(database);


        highscores = new HighscoreFactory(database);

        database.withConnection(conn -> {
            UserFactory.UserImplementation user = null;
            String sql = "INSERT INTO user (iduser, name, password, email) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, Integer.toString(ID_USER));
            stmt.setString(2, NAME_USER);
            stmt.setString(3, PASSWORD_USER);
            stmt.setString(4, EMAIL_USER);
            stmt.executeUpdate();
            stmt.close();
        });

        database.withConnection(conn -> {
            UserFactory.UserImplementation user = null;
            String sql = "INSERT INTO quiz (idQuiz, name) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, Integer.toString(ID_QUIZ));
            stmt.setString(2, NAME_QUIZ);
            stmt.executeUpdate();
            stmt.close();
        });

        database.withConnection(conn -> {
            UserFactory.UserImplementation user = null;
            String sql = "INSERT INTO highscores (quiz_idQuiz, user_iduser, highscore) VALUES ( ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, Integer.toString(ID_QUIZ));
            stmt.setString(2, Integer.toString(ID_USER));
            stmt.setString(3, Integer.toString(SCORE_HIGHSCORE));
            stmt.executeUpdate();
        });
    }

    @Test
    public void fetchedHighscoreShouldContainValues(){
        Highscore highscore = highscores.getHighscoresOfUser(ID_USER).get(0);


        assertNotNull(highscore);
        assertEquals(ID_USER, highscore.getUserId());
        assertEquals(SCORE_HIGHSCORE, highscore.getScore());
        assertEquals(ID_QUIZ, highscore.getQuizId());
        assertEquals(NAME_USER, highscore.getUserName());
        assertEquals(NAME_QUIZ, highscore.getQuizName());

    }
}
