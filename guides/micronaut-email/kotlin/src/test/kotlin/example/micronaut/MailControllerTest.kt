package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest // <1>
@Property(name = "spec.name", value = "mailcontroller") // <2>
internal class MailControllerTest {

    @Inject
    lateinit var applicationContext: ApplicationContext // <3>

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient // <4>

    @Test
    fun mailsendInteractsOnceEmailService() {
        val cmd = EmailCmd()
        cmd.subject = "Test"
        cmd.recipient = "delamos@grails.example"
        cmd.textBody = "Hola hola"

        val request: HttpRequest<EmailCmd> = HttpRequest.POST("/mail/send", cmd) // <5>
        val emailServices = applicationContext.getBeansOfType(EmailService::class.java)
        Assertions.assertEquals(1, emailServices.size)

        val emailService = applicationContext.getBean(EmailService::class.java)
        Assertions.assertTrue(emailService is MockEmailService)

        val oldEmailsSize = (emailService as MockEmailService).emails.size
        val rsp: HttpResponse<*> = client.toBlocking().exchange<EmailCmd, Any>(request)
        Assertions.assertEquals(HttpStatus.OK, rsp.status)
        Assertions.assertEquals(oldEmailsSize + 1, emailService.emails.size) // <6>
    }
}