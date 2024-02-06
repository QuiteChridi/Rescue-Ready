package models;

import controllers.interfaces.AbstractUserFactory;
import org.junit.After;
import play.db.Database;
import org.junit.Before;
import play.test.WithApplication;

import static org.junit.Assert.*;

public class UserFactoryTest extends WithApplication {

    private AbstractUserFactory users;
    private Database db;
    private final String TEST_NAME = "test03945830958";
    private final String TEST_PASSWORD = "test";
    private final String TEST_EMAIL = "test@test.de";

    @Before
    public void setUp() {
        users = provideApplication().injector().instanceOf(UserFactory.class);
    }

    @After
    public void tearDown() {
        //db.shutdown();
    }
}