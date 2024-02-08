package integrationTests;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.interfaces.User;
import models.HighscoreFactory;
import models.UserFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import views.html.login;
import views.html.signup;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class ApplicationTests extends WithApplication {

    private UserFactory userFactory;

    @Before
    public void setUp(){
        userFactory = provideApplication().injector().instanceOf(UserFactory.class);
    }

    @Test
    public void loginTestUserShouldBeAbleToLogInWithValidCredentials(){
        User validUser = userFactory.getUserById("1");
        ObjectNode requestBody = Json.newObject();
        requestBody.put("username", validUser.getName());
        requestBody.put("password", validUser.getPassword());

        Http.RequestBuilder request = fakeRequest()
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .method(POST)
                .uri("/authenticate")
                .bodyJson(requestBody);

        Result result = route(app, request);

        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Login successful"));
        assertEquals(validUser.getId(), Integer.parseInt(result.session().get("userID").orElse(" ")));
    }

    @Test
    public void loginTestUserShouldNotBeAbleToLogInWithInvalidName(){
        User invalidNameUser = userFactory.getUserById("1");
        invalidNameUser.setName(invalidNameUser.getName() + "veryInvalid");

        ObjectNode requestBody = Json.newObject();
        requestBody.put("username", invalidNameUser.getName());
        requestBody.put("password", invalidNameUser.getPassword());
        Http.RequestBuilder request = fakeRequest()
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .method(POST)
                .uri("/authenticate")
                .bodyJson(requestBody);

        Result result = route(app, request);


        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Wrong username or password"));
    }


    @Test
    public void loginTestUserShouldNotBeAbleToLogInWithInvalidPassword(){
        User invalidPasswordUser = userFactory.getUserById("1");
        invalidPasswordUser.setPassword(invalidPasswordUser.getPassword() + "**");

        ObjectNode requestBody = Json.newObject();
        requestBody.put("username", invalidPasswordUser.getName());
        requestBody.put("password", invalidPasswordUser.getPassword());
        Http.RequestBuilder request = fakeRequest()
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .method(POST)
                .uri("/authenticate")
                .bodyJson(requestBody);

        Result result = route(app, request);


        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Wrong username or password"));
    }

    @Test
    public void testIndexContainsLoginLink() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .uri("/");
        Result result = route(app, request);
        final String body = contentAsString(result);
        assertTrue("Index should contain a Login link", body.contains("value=\"Login\""));
    }

    @Test
    public void mainShouldReturnLoginPage() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .uri("/");

        Result result = route(app, request);
        assertEquals(contentAsString(login.render()), contentAsString(result));
    }

    @Test
    public void signupShouldReturnSignupPage() {
        Http.RequestBuilder requestBuilder = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .uri("/signup");
        Result result = route(app, requestBuilder);
        assertEquals(contentAsString(signup.render()), contentAsString(result));
    }
}
