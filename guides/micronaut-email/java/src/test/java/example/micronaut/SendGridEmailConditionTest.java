package example.micronaut;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SendGridEmailConditionTest {

    @Test
    public void conditionIsTrueIfSystemPropertiesArePresent() {

        String sendGridApiKey = System.getProperty("sendgrid.apikey");
        String sendGrindFromEmail = System.getProperty("sendgrid.fromemail");
        System.setProperty("sendgrid.apikey", "XXXX");
        System.setProperty("sendgrid.fromemail", "me@micronaut.example");

        SendGridEmailCondition condition = new SendGridEmailCondition();
        assertTrue(condition.matches(null));

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

    @Test
    public void conditionIsFalseIfSystemPropertiesAreNotPresent() {
        SendGridEmailCondition condition = new SendGridEmailCondition();
        assertFalse(condition.matches(null));
    }
}
