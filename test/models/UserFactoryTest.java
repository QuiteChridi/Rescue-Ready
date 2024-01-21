package models;

import org.junit.After;
import play.db.Database;
import org.junit.Before;
import org.junit.Test;
import play.test.WithApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.*;

public class UserFactoryTest extends WithApplication {

    private UserFactory users;
    private Database db;
    private final String TEST_NAME = "test03945830958";
    private final String TEST_PASSWORD = "test";
    private final String TEST_EMAIL = "test@test.de";

    @Before
    public void setUp() {
        users = provideApplication().injector().instanceOf(UserFactory.class);
        db = provideApplication().injector().instanceOf(Database.class);

        db.withConnection(conn -> {
            UserFactory.User user = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE name = ? AND password = ?");
            stmt.setString(1, TEST_NAME);
            stmt.setString(2, TEST_PASSWORD);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                throw new IllegalArgumentException("Test User already exists");
            }

            stmt = conn.prepareStatement("INSERT INTO user (name, password, email) VALUES ( ?, ?, ?)");
            stmt.setString(1, TEST_NAME);
            stmt.setString(2, TEST_PASSWORD);
            stmt.setString(3, TEST_EMAIL);
            stmt.executeUpdate();

            stmt.close();
        });
    }

    @After
    public void tearDown() {
        db.withConnection(conn -> {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM user WHERE name = ?");
            stmt.setString(1, TEST_NAME);
            stmt.executeUpdate();
            stmt.close();

        });
    }

    @Test
    public void authenticateWithTestNameAndPasswordShouldReturnAUser() {
        UserFactory.User user = users.authenticate(TEST_NAME, TEST_PASSWORD);
        assertNotNull(user);
    }

    @Test
    public void authenticateNoneShouldReturnNoUser() {
        UserFactory.User user = users.authenticate("", "");
        assertNull(user);
    }

    @Test
    public void authenticateWithoutPasswordShouldReturnNoUser() {
        UserFactory.User user = users.authenticate(TEST_NAME, "");
        assertNull(user);
    }
}