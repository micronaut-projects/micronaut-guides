package example.micronaut;

import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SendGridEmailServiceTest {

    @Test
    public void sendGridEmailServiceIsNotLoadedIfSystemPropertyIsNotPresent() {
        ApplicationContext ctx = ApplicationContext.run();
        assertFalse(ctx.containsBean(SendGridEmailService.class));
        ctx.close();
    }

    @Test
    public void sendGridEmailServiceIsLoadedIfSystemPropertiesArePresent() {
        String sendGridApiKey = System.getProperty("sendgrid.apikey");
        String sendGrindFromEmail = System.getProperty("sendgrid.fromemail");
        System.setProperty("sendgrid.apikey", "XXXX");
        System.setProperty("sendgrid.fromemail", "me@micronaut.example");
        ApplicationContext ctx = ApplicationContext.run();
        SendGridEmailService bean = ctx.getBean(SendGridEmailService.class);
        assertEquals("XXXX", bean.apiKey);
        assertEquals("me@micronaut.example", bean.fromEmail);
        ctx.close();
        if (sendGridApiKey == null) {
            System.clearProperty("sendgrid.apikey");
        } else {
            System.setProperty("sendgrid.apikey", sendGridApiKey);
        }
        if (sendGrindFromEmail == null) {
            System.clearProperty("sendgrid.fromemail");
        } else {
            System.setProperty("sendgrid.fromemail", sendGrindFromEmail);
        }

    }
}
