package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.UserFactory;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Http.*;
import play.mvc.Result;
import views.html.login;
import views.html.signup;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static play.test.Helpers.*;

/**
 * This class contains tests for the LoginController.
 * The tests are written using the JUnit testing framework.
 */
public class LoginControllerTest {

    /**
     * This test checks if the login page is returned when the login method is called.
     * It uses the contentAsString method to compare the content of the page with the expected content.
     * The login method is called by creating a new instance of the LoginController class and calling the login method on it.
     */
    @Test
    public void loginShouldReturnLoginPage() {
        UserFactory userFactoryDummy = mock(UserFactory.class);
        LoginController loginController = new LoginController(userFactoryDummy);

        assertEquals(contentAsString(login.render()), contentAsString(loginController.login()));
    }

    /**
     * This test checks if the signup page is returned when the signUp method is called.
     * It uses the contentAsString method to compare the content of the page with the expected content.
     * The signUp method is called by creating a new instance of the LoginController class and calling the signUp method on it.
     */
    @Test
    public void signupShouldReturnSignupPage() {
        UserFactory userFactoryDummy = mock(UserFactory.class);
        LoginController loginController = new LoginController(userFactoryDummy);

        assertEquals(contentAsString(signup.render()), contentAsString(loginController.signUp()));
    }

    /**
     * This test checks if the authenticate method returns an error message if the verify method returns null.
     * It uses the contentAsString method to compare the content of the page with the expected content.
     * The authenticate method is called by creating a new instance of the LoginController class and calling the authenticate method on it.
     */
    @Test
    public void authenticateShouldReturnErrorMessageIfVerifyReturnsNull() {
        UserFactory userFactoryDummy = mock(UserFactory.class);
        LoginController loginController = new LoginController(userFactoryDummy);

        ObjectNode requestBody = Json.newObject();
        requestBody.put("username", " ");
        requestBody.put("password", " ");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .bodyJson(requestBody)
                .uri("/");

        Result result = loginController.authenticate(request.build());
        assertTrue(contentAsString(result).contains("Wrong username or password"));

        verify(userFactoryDummy).authenticate(" ", " ");
    }

    /**
     * This test checks if the authenticate method returns a successful login message if the verify method returns a user.
     * It uses the contentAsString method to compare the content of the page with the expected content.
     * The authenticate method is called by creating a new instance of the LoginController class and calling the authenticate method on it.
     */
    @Test
    public void authenticateShouldReturnSuccessfulLoginIfVerifyReturnsUser() {
        UserFactory.UserImplementation userStub = mock(UserFactory.UserImplementation.class);
        when(userStub.getId()).thenReturn(1);
        UserFactory userFactoryStub = mock(UserFactory.class);
        when(userFactoryStub.authenticate(" ", " ")).thenReturn(userStub);
        LoginController loginController = new LoginController(userFactoryStub);

        ObjectNode requestBody = Json.newObject();
        requestBody.put("username", " ");
        requestBody.put("password", " ");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .bodyJson(requestBody)
                .uri("/");

        Result result = loginController.authenticate(request.build());
        assertTrue(contentAsString(result).contains("Login successful"));
        assertEquals("1" , result.session().get("userID").orElse(" "));
        verify(userFactoryStub).authenticate(" ", " ");
    }

    /**
     * This test checks if the logout method resets the session.
     * It uses the session method to get the session and checks if the size of the session data is 0.
     * The logout method is called by creating a new instance of the LoginController class and calling the logout method on it.
     */
    @Test
    public void logoutShouldResetSession() {
        UserFactory userFactoryDummy = mock(UserFactory.class);
        LoginController loginController = new LoginController(userFactoryDummy);

        Session session = loginController.logout().session();

        assertEquals(0,  session.data().size());
    }

    /**
     * This test checks if the createUser method returns a not acceptable message if the user already exists.
     * It uses the status method to compare the status of the result with the expected status.
     * The createUser method is called by creating a new instance of the LoginController class and calling the createUser method on it.
     */
    @Test
    public void createUserShouldReturnNotAcceptableIfUserExists() {
        UserFactory userFactoryDummy = mock(UserFactory.class);
        LoginController loginController = new LoginController(userFactoryDummy);

        ObjectNode requestBody = Json.newObject();
        requestBody.put("username", " ");
        requestBody.put("password", " ");
        requestBody.put("email", " ");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .bodyJson(requestBody)
                .uri("/");

        Result result = loginController.createUser(request.build());
        assertEquals(406,result.status());
        verify(userFactoryDummy).createUser(" ", " ", " ");
    }

    /**
     * This test checks if the createUser method adds the user to the session if the user does not exist.
     * It uses the session method to get the session and checks if the session contains the user.
     * The createUser method is called by creating a new instance of the LoginController class and calling the createUser method on it.
     */
    @Test
    public void createUserShouldAddUserToSessionIfUsernameNotExists() {
        UserFactory.UserImplementation dummyUser= mock(UserFactory.UserImplementation.class);
        UserFactory userFactoryMock = mock(UserFactory.class);
        when(userFactoryMock.createUser(" ", " ", " ")).thenReturn(dummyUser);
        LoginController loginController = new LoginController(userFactoryMock);

        ObjectNode requestBody = Json.newObject();
        requestBody.put("username", " ");
        requestBody.put("password", " ");
        requestBody.put("email", " ");

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .bodyJson(requestBody)
                .uri("/");

        Map<String,String> session = loginController.createUser(request.build()).session().data();

        verify(userFactoryMock).createUser(" ", " ", " ");
        assertTrue(session.containsKey("userID"));
    }
}
