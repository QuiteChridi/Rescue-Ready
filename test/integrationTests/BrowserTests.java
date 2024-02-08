package integrationTests;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithBrowser;

import static org.junit.Assert.*;

public class BrowserTests extends WithBrowser {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }


    @Test
    public void loginWithValidCredentials() {
        browser.goTo("/");
        browser.$("#username").fill().with(" ");
        browser.$("#password").fill().with(" ");
        browser.$("#submit_login").submit();


        assertTrue(browser.window().title().contains("quizSelection"));
    }

}
