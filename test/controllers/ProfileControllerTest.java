package controllers;

import controllers.ProfileController;
import controllers.interfaces.Highscore;
import models.*;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import views.html.login;
import views.html.profile;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static play.mvc.Results.redirect;
import static play.test.Helpers.*;

/**
 * This class contains tests for the ProfileController.
 * The tests are written using the JUnit testing framework.
 * It extends the WithApplication class to provide a fake application for the tests.
 */
public class ProfileControllerTest extends WithApplication {

    private UserFactory.UserImplementation userStub;
    private List<Highscore> highscoreListStub;
    private ProfileController profileController;

    /**
     * This method is called before each test to set up the test environment.
     * It initializes the userStub attribute with a new instance of UserFactory.UserImplementation and the highscoreListStub attribute with a new LinkedList.
     * It also initializes the userFactoryStub attribute with a new instance of UserFactory and the highscoreFactoryStub attribute with a new instance of HighscoreFactory.
     * It uses the when method to specify the behavior of the userFactoryStub and highscoreFactoryStub attributes when their methods are called.
     */
    @Before
    public  void setUp(){
        userStub = mock(UserFactory.UserImplementation.class);
        highscoreListStub = new LinkedList<>();

        UserFactory userFactoryStub = mock(UserFactory.class);
        when(userFactoryStub.getUserById(0)).thenReturn(userStub);

        HighscoreFactory highscoreFactoryStub = mock(HighscoreFactory.class);
        when(highscoreFactoryStub.getHighscoresOfUser(0)).thenReturn(highscoreListStub);

        profileController = new ProfileController(userFactoryStub, highscoreFactoryStub);

    }

    /**
     * This test checks if the profile page is returned when the profile method is called.
     * It uses the contentAsString method to compare the content of the page with the expected content.
     * The profile method is called by creating a new instance of the ProfileController class and calling the profile method on it.
     */
    @Test
    public void profileShouldReturnProfileIfUserInSession() {

        Http.Request requestWithUser = fakeRequest().session("userID", "0").build();

        Result result = profileController.profile(requestWithUser);
        assertEquals(contentAsString(profile.render(userStub, highscoreListStub)), contentAsString(result));
    }

    /**
     * This test checks if the profile method returns a redirect if no user is in the session.
     * The profile method is called by creating a new instance of the ProfileController class and calling the profile method on it.
     */
    @Test
    public void profileShouldReturnRedirectIfNoUserInSession() {
        Http.Request requestWithoutUser = fakeRequest().build();

        Result result = profileController.profile(requestWithoutUser);

        assertEquals(303, result.status());
    }
}
