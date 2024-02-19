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

public class ProfileControllerTest extends WithApplication {
    private UserFactory userFactoryStub;
    private HighscoreFactory highscoreFactoryStub;
    private QuizFactory quizFactoryDummy;
    private UserFactory.UserImplementation userStub;
    private List<Highscore> highscoreListStub;
    private ProfileController profileController;


    @Before
    public  void setUp(){
        userStub = mock(UserFactory.UserImplementation.class);
        highscoreListStub = new LinkedList<>();

        userFactoryStub = mock(UserFactory.class);
        when(userFactoryStub.getUserById(0)).thenReturn(userStub);

        highscoreFactoryStub = mock(HighscoreFactory.class);
        when(highscoreFactoryStub.getHighscoresOfUser(0)).thenReturn(highscoreListStub);

        profileController = new ProfileController(userFactoryStub, highscoreFactoryStub);

    }

    @Test
    public void profileShouldReturnProfileIfUserInSession() {

        Http.Request requestWithUser = fakeRequest().session("userID", "0").build();

        Result result = profileController.profile(requestWithUser);
        assertEquals(contentAsString(profile.render(userStub, highscoreListStub)), contentAsString(result));
    }

    @Test
    public void profileShouldReturnRedirectIfNoUserInSession() {
        Http.Request requestWithoutUser = fakeRequest().build();

        Result result = profileController.profile(requestWithoutUser);

        assertEquals(303, result.status());
    }


}
