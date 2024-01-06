package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import play.test.WithBrowser;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class HomeControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testIndex() {
        //Simulates HTTP Request to Server
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .uri("/");

        //Routes simulated Request to Play Application based on URI + Method
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testLogin() {
        Http.RequestBuilder requestBuilder = new Http.RequestBuilder()
                .method(GET)
                .header(Http.HeaderNames.HOST, "localhost:19001")
                .uri("/login");
        Result result = route(app, requestBuilder);
        assertEquals(OK, result.status());
    }

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
}
