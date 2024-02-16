package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.LoginController;
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


public class LoginControllerTest {

    @Test
    public void loginShouldReturnLoginPage() {
        UserFactory userFactoryDummy = mock(UserFactory.class);
        LoginController loginController = new LoginController(userFactoryDummy);

        assertEquals(contentAsString(login.render()), contentAsString(loginController.login()));
    }

    @Test
    public void signupShouldReturnSignupPage() {
        UserFactory userFactoryDummy = mock(UserFactory.class);
        LoginController loginController = new LoginController(userFactoryDummy);

        assertEquals(contentAsString(signup.render()), contentAsString(loginController.signUp()));
    }

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

    @Test
    public void logoutShouldResetSession() {
        UserFactory userFactoryDummy = mock(UserFactory.class);
        LoginController loginController = new LoginController(userFactoryDummy);

        Session session = loginController.logout().session();

        assertEquals(0,  session.data().size());
    }

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
        verify(userFactoryDummy).createUserInUsers(" ", " ", " ");
    }

    @Test
    public void createUserShouldAddUserToSessionIfUsernameNotExists() {
        UserFactory.UserImplementation dummyUser= mock(UserFactory.UserImplementation.class);
        UserFactory userFactoryMock = mock(UserFactory.class);
        when(userFactoryMock.createUserInUsers(" ", " ", " ")).thenReturn(dummyUser);
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

        verify(userFactoryMock).createUserInUsers(" ", " ", " ");
        assertTrue(session.containsKey("userID"));
    }
}
