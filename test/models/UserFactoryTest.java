package models;
import controllers.interfaces.User;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import play.db.Database;
import org.junit.Before;
import play.db.Databases;
import play.db.evolutions.Evolutions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * Testclass for UserFactory
 */
public class UserFactoryTest {
    private Database database;
    private UserFactory users;
    private String NAME_USER_1 = "testuser";
    private String PASSWORD_USER_1 = "testpassword";
    private String EMAIL_USER_1 = "test@test.de";
    private int ID_USER_1;
    private String NEW_USERNAME = "othertestuser";
    private String NEW_PASSWORD = "othertestpassword";
    private String NEW_EMAIL = "othertest@test.de";
    private String INVALID_USERNAME = "invalid";
    private String INVALID_PASSWORD = "invalid";
    private int INVALID_ID = 999;

    /**
     * Given a database with an user
     * When the database is set up
     * Then it should contain the user
     */
    @Before
    public void givenADatabaseWithAnUser(){
        database = Databases.inMemory("default");
        Evolutions.applyEvolutions(database);
        users = new UserFactory(database);

        System.out.println(database.getUrl());
        database.withConnection(conn -> {
            UserFactory.UserImplementation user = null;
            String sql = "INSERT INTO user (name, password, email) VALUES ( ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, NAME_USER_1);
            stmt.setString(2, PASSWORD_USER_1);
            stmt.setString(3, EMAIL_USER_1);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ID_USER_1 = rs.getInt(1);
            }
            stmt.close();
        });
    }

    /**
     * The authenticate method should return the user for a valid username and password
     */
    @Test
    public void authenticateShouldReturnUserForValidUsernameAndPassword(){
        User user = users.authenticate(NAME_USER_1, PASSWORD_USER_1);

        assertNotNull(user);
    }

    /**
     * The authenticate method should return null for a INvalid username
     */
    @Test
    public void authenticateShouldReturnNullForInValidUsername(){
        User user = users.authenticate(INVALID_USERNAME, PASSWORD_USER_1);

        assertNull(user);
    }

    /**
     * The authenticate method should return null for a Invalid password
     */
    @Test
    public void authenticateShouldReturnNullForInValidPassword(){
        User user = users.authenticate(NAME_USER_1, INVALID_PASSWORD);

        assertNull(user);
    }

    /**
     * The authenticate method should return null for a Invalid username and password
     */
    @Test
    public void createUserInUsersShouldReturnCreatedUser(){
        User newUser = users.createUser(NEW_USERNAME, NEW_PASSWORD, NEW_EMAIL);
        User fetchedUser = users.getUserById(newUser.getId());

        assertNotNull(newUser);
        assertEquals(fetchedUser.getName(), newUser.getName());
        assertEquals(fetchedUser.getPassword(), newUser.getPassword());
        assertEquals(fetchedUser.getMail(), newUser.getMail());
    }

    /**
     * The Id of the user should be returned by getId if the user exists in the database
     */
    @Test
    public void getUserByIdShouldReturnUserForValidID(){
        User user = users.getUserById(ID_USER_1);

        assertNotNull(user);
        assertEquals(NAME_USER_1, user.getName());
        assertEquals(PASSWORD_USER_1, user.getPassword());
        assertEquals(EMAIL_USER_1, user.getMail());
    }

    /**
     * The getUserById method should return null for an invalid ID
     */
    @Test
    public void getUserByIdShouldReturnNullForInvalidID(){
        User user = users.getUserById(INVALID_ID);

        assertNull(user);
    }

    /**
     * The tearDown method should shut down the database
     */
    @After
    public void tearDown() {
        database.shutdown();
    }
}