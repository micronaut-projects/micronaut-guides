package example.micronaut

import io.micronaut.context.ApplicationContext
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

class SendGridEmailServiceSpec extends Specification {

    void "send grid email service is not loaded if system property is not present"() {
        given:
        ApplicationContext ctx = ApplicationContext.run()

        expect:
        !ctx.containsBean(SendGridEmailService.class)

        cleanup:
        ctx.close()
    }

    @RestoreSystemProperties
    void "send grid email service is loaded if system properties are present"() {
        given:
        System.setProperty("sendgrid.apikey", "XXXX")
        System.setProperty("sendgrid.fromemail", "me@micronaut.example")
        ApplicationContext ctx = ApplicationContext.run()
        SendGridEmailService bean = ctx.getBean(SendGridEmailService.class)

        expect:
        "XXXX" == bean.apiKey
        "me@micronaut.example" == bean.fromEmail

        cleanup:
        ctx.close()

    }
}
