package models;

import controllers.interfaces.Quiz;
import controllers.interfaces.User;
import org.junit.After;
import org.junit.Test;
import play.db.Database;
import org.junit.Before;
import play.db.Databases;
import play.db.evolutions.Evolutions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

public class QuizFactoryTest {
    private Database database;
    private QuizFactory quizzes;

    private String NAME_USER_1 = "testuser";
    private String PASSWORD_USER_1 = "testpassword";
    private String EMAIL_USER_1 = "test@test.de";
    private int QUIZ_ID;
    private String QUIZ_NAME = "MyFirstQuiz";

    @Before
    public void givenADatabaseWithAQuiz() {
        database = Databases.inMemory("default");
        Evolutions.applyEvolutions(database);
        quizzes = new QuizFactory(database);

        System.out.println(database.getUrl());
        database.withConnection(conn -> {
            UserFactory.UserImplementation user = null;
            String sql = "INSERT INTO quiz (name) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, QUIZ_NAME);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                QUIZ_ID = rs.getInt("idQuiz");
            }
            stmt.close();
        });
    }

    @Test
    public void getQuizByIdShouldReturnValidQuiz() {
        Quiz quiz = quizzes.getQuizById(QUIZ_ID);

        assertNotNull(quiz);
        assertEquals(QUIZ_NAME, quiz.getName());
    }

    @Test
    public void getQuizByIdShouldReturnNullForInvalidId() {
        Quiz quiz = quizzes.getQuizById(0);

        assertNull(quiz);
    }

    @After
    public void tearDown() {
        database.shutdown();
    }
}