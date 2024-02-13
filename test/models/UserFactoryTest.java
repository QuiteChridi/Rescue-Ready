package models;
import org.junit.After;
import org.junit.Test;
import play.db.Database;
import org.junit.Before;
import play.db.Databases;
import play.test.WithApplication;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserFactoryTest {
    private Database database;
    private UserFactory users;


    @Before
    public void setUp() {
        database = Databases.inMemory("inMemory");
        users = new UserFactory(database);
    }

    @Test
    public void testDatabase() throws SQLException {
        users.getUserById(1);
    }
    @After
    public void tearDown() {
        database.shutdown();
    }
}