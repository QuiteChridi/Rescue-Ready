package controllers;

import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import views.html.login;
import views.html.signup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;
import static play.test.Helpers.contentAsString;

public class RequestTest extends WithApplication {
    @Test
    public void testTemplateContainsJQuery() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .uri("/");
        Result result = route(app, request);
        final String body = contentAsString(result);
        assertTrue("Template should contain jQuery", body.contains("<script src=\"https://code.jquery.com/jquery-3.6.0.min.js\"></script>"));
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
