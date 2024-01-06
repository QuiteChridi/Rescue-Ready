package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import play.test.WithBrowser;

public class BrowserTest extends WithBrowser {
    @Test
    public void testBrowser() {
        browser.goTo("/");
        browser.$("input[type='button'][value='login']").click();
        assertEquals("/login", browser.url());
    }
}
