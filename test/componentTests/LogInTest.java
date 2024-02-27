package componentTests;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.interfaces.User;
import models.UserFactory;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import views.html.login;
import views.html.signup;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

/**
 * This class contains tests for the LoginController.
 */
public class
LogInTest extends WithApplication {

    /**
     * This test checks if the login page is returned when the main method is called.
     */
    @Test
    public void mainShouldReturnLoginPage() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .uri("/");

        Result result = route(app, request);
        assertEquals(contentAsString(login.render()), contentAsString(result));
    }

    /**
     * This test checks if the signup page is returned when the signup method is called.
     * The signup page should be returned when the signup method is called.
     */
    @Test
    public void signupShouldReturnSignupPage() {
        Http.RequestBuilder requestBuilder = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .uri("/signup");
        Result result = route(app, requestBuilder);
        assertEquals(contentAsString(signup.render()), contentAsString(result));
    }

    /**
     * This test checks if the login page is returned when the login method is called.
     * The login page should be returned when the login method is called.
     */
    @Test
    public void loginTestUserShouldBeAbleToLogInWithValidCredentials(){
        UserFactory userFactory = provideApplication().injector().instanceOf(UserFactory.class);
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

    /**
     * This test checks if the login page is returned when the login method is called.
     * The login page should be returned when the login method is called.
     */
    @Test
    public void loginTestUserShouldNotBeAbleToLogInWithInvalidName(){
        UserFactory userFactory = provideApplication().injector().instanceOf(UserFactory.class);
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

    /**
     * This test checks if the login page is returned when the login method is called.
     * The login page should be returned when the login method is called.
     */
    @Test
    public void loginTestUserShouldNotBeAbleToLogInWithInvalidPassword(){
        UserFactory userFactory = provideApplication().injector().instanceOf(UserFactory.class);
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

    /**
     * This test checks if the login page is returned when the login method is called.
     * The login page should be returned when the login method is called.
     */
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
}
