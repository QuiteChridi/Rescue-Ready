package models;
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

    @Before
    public void givenADatabaseWithAnUser(){
        database = Databases.inMemory("inMemory");
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

    @Test
    public void authenticateShouldReturnUserForValidUsernameAndPassword(){
        User user = users.authenticate(NAME_USER_1, PASSWORD_USER_1);

        assertNotNull(user);
    }

    @Test
    public void authenticateShouldReturnNullForInValidUsername(){
        User user = users.authenticate(INVALID_USERNAME, PASSWORD_USER_1);

        assertNull(user);
    }

    @Test
    public void authenticateShouldReturnNullForInValidPassword(){
        User user = users.authenticate(NAME_USER_1, INVALID_PASSWORD);

        assertNull(user);
    }

    @Test
    public void createUserInUsersShouldReturnCreatedUser(){
        User newUser = users.createUser(NEW_USERNAME, NEW_PASSWORD, NEW_EMAIL);
        User fetchedUser = users.getUserById(newUser.getId());

        assertNotNull(newUser);
        assertEquals(fetchedUser.getName(), newUser.getName());
        assertEquals(fetchedUser.getPassword(), newUser.getPassword());
        assertEquals(fetchedUser.getMail(), newUser.getMail());
    }

    @Test
    public void getUserByIdShouldReturnUserForValidID(){
        User user = users.getUserById(ID_USER_1);

        assertNotNull(user);
        assertEquals(NAME_USER_1, user.getName());
        assertEquals(PASSWORD_USER_1, user.getPassword());
        assertEquals(EMAIL_USER_1, user.getMail());
    }

    @Test
    public void getUserByIdShouldReturnNullForInvalidID(){
        User user = users.getUserById(INVALID_ID);

        assertNull(user);
    }


    @After
    public void tearDown() {
        database.shutdown();
    }
}