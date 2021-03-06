package example.micronaut

import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class SendGridEmailServiceTest {

    @Test
    fun sendGridEmailServiceIsNotLoadedIfSystemPropertyIsNotPresent() {
        val ctx = ApplicationContext.run()
        assertFalse(ctx.containsBean(SendGridEmailService::class.java))
        ctx.close()
    }

    @Test
    fun sendGridEmailServiceIsLoadedIfSystemPropertiesArePresent() {
        val sendGridApiKey = System.getProperty("sendgrid.apikey")
        val sendGrindFromEmail = System.getProperty("sendgrid.fromemail")
        System.setProperty("sendgrid.apikey", "XXXX")
        System.setProperty("sendgrid.fromemail", "me@micronaut.example")

        val ctx = ApplicationContext.run()
        val bean = ctx.getBean(SendGridEmailService::class.java)

        assertEquals("XXXX", bean.apiKey)
        assertEquals("me@micronaut.example", bean.fromEmail)

        ctx.close()

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
}
