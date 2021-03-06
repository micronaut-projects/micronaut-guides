package example.micronaut

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SendGridEmailConditionTest {

    @Test
    fun conditionIsTrueIfSystemPropertiesArePresent() {
        val sendGridApiKey = System.getProperty("sendgrid.apikey")
        val sendGrindFromEmail = System.getProperty("sendgrid.fromemail")
        System.setProperty("sendgrid.apikey", "XXXX")
        System.setProperty("sendgrid.fromemail", "me@micronaut.example")

        val condition = SendGridEmailCondition()
        assertTrue(condition.matches(null))

        if (sendGridApiKey == null) {
            System.clearProperty("sendgrid.apikey")
        } else {
            System.setProperty("sendgrid.apikey", sendGridApiKey)
        }
        if (sendGrindFromEmail == null) {
            System.clearProperty("sendgrid.fromemail")
        } else {
            System.setProperty("sendgrid.fromemail", sendGrindFromEmail)
        }
    }

    @Test
    fun conditionIsFalseIfSystemPropertiesAreNotPresent() {
        val sendGridApiKey = System.getProperty("sendgrid.apikey")
        val sendGrindFromEmail = System.getProperty("sendgrid.fromemail")

        val condition = SendGridEmailCondition()
        assertFalse(condition.matches(null))
    }
}
