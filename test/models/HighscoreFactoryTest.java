package models;

import controllers.interfaces.Highscore;
import controllers.interfaces.User;
import org.junit.After;
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
import java.util.Map;

public class HighscoreFactoryTest {
    private HighscoreFactory highscores;
    private Database database;

    private final String NAME_USER = "testuser";
    private final String PASSWORD_USER = "testpassword";
    private final String EMAIL_USER = "test@test.de";
    private final int ID_USER = 99;
    private final String NAME_QUIZ = "testquiz";
    private final int ID_QUIZ = 99;
    private final int SCORE_HIGHSCORE = 100;


    @Before
    public void givenADatabaseWithAnUserAnQuizAndAnHighscore(){
        database = Databases.inMemory("inMemory");
        Evolutions.applyEvolutions(database);


        highscores = new HighscoreFactory(database);

        database.withConnection(conn -> {
            String sql = "DELETE FROM user ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
        });

        database.withConnection(conn -> {
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
            String sql = "DELETE FROM quiz ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
        });

        database.withConnection(conn -> {
            String sql = "INSERT INTO quiz (idQuiz, name) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, Integer.toString(ID_QUIZ));
            stmt.setString(2, NAME_QUIZ);
            stmt.executeUpdate();
            stmt.close();
        });
        database.withConnection(conn -> {
            String sql = "DELETE FROM highscores ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();
        });
        database.withConnection(conn -> {
            String sql = "INSERT INTO highscores (quiz_idQuiz, user_iduser, highscore) VALUES ( ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, Integer.toString(ID_QUIZ));
            stmt.setString(2, Integer.toString(ID_USER));
            stmt.setString(3, Integer.toString(SCORE_HIGHSCORE));
            stmt.executeUpdate();
        });
    }

    @Test
    public void getPossibleQuizNamesShouldReturnAllQuizNames(){
        Map<Integer, String> quizNames = highscores.getPossibleQuizNames();

        assertNotNull(quizNames);
        assertEquals(1, quizNames.size());
        assertEquals(NAME_QUIZ, quizNames.get(ID_QUIZ));
    }

    @Test
    public void getHighscoreOfQuizShouldReturnAllHighscoresOfAQuiz(){
        List<Highscore> scoresOfQuiz = highscores.getHighscoresOfQuiz(ID_QUIZ);

        assertNotNull(scoresOfQuiz);
        assertEquals(1, scoresOfQuiz.size());
    }

    @Test
    public void getHighscoreOfUSERShouldReturnAllHighscoresOfAnUser(){
        List<Highscore> scoresOfUser = highscores.getHighscoresOfUser(ID_USER);

        assertNotNull(scoresOfUser);
        assertEquals(1, scoresOfUser.size());
    }

    @Test
    public void fetchedHighscoreByUserAndQuizIdShouldContainValues(){
        Highscore highscore = highscores.getHighscoreOfUserAndQuiz(ID_USER, ID_QUIZ);


        assertNotNull(highscore);
        assertEquals(ID_USER, highscore.getUserId());
        assertEquals(SCORE_HIGHSCORE, highscore.getScore());
        assertEquals(ID_QUIZ, highscore.getQuizId());
        assertEquals(NAME_USER, highscore.getUserName());
        assertEquals(NAME_QUIZ, highscore.getQuizName());
    }


    @After
    public void tearDown() {
        database.shutdown();
    }
}
