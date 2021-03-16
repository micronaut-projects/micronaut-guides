package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import javax.inject.Inject

@MicronautTest // <1>
@Property(name = "spec.name", value = "mailcontroller") // <2>
class MailControllerValidationTest {

    @Inject
    lateinit var applicationContext: ApplicationContext

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Test
    fun mailSendCannotBeInvokedWithoubSubject() {
        val cmd = EmailCmd()
        cmd.recipient = "delamos@micronaut.example"
        cmd.textBody = "Hola hola"

        val request: HttpRequest<EmailCmd> = HttpRequest.POST("/mail/send", cmd) // <3>
        val e = Executable { client.toBlocking().exchange<EmailCmd, Any>(request) }
        val thrown = Assertions.assertThrows(HttpClientResponseException::class.java, e)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    @Test
    fun mailSendCannotBeInvokedWithoutRecipient() {
        val cmd = EmailCmd()
        cmd.subject = "Hola"
        cmd.textBody = "Hola hola"

        val request: HttpRequest<EmailCmd> = HttpRequest.POST("/mail/send", cmd) // <3>
        val e = Executable { client.toBlocking().exchange<EmailCmd, Any>(request) }
        val thrown = Assertions.assertThrows(HttpClientResponseException::class.java, e)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    @Test
    fun mailSendCannotBeInvokedWithoutEitherTextBodyOrHtmlBody() {
        val cmd = EmailCmd()
        cmd.subject = "Hola"
        cmd.recipient = "delamos@micronaut.example"

        val request: HttpRequest<EmailCmd> = HttpRequest.POST("/mail/send", cmd) // <3>
        val e = Executable { client.toBlocking().exchange<EmailCmd, Any>(request) }
        val thrown = Assertions.assertThrows(HttpClientResponseException::class.java, e)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    @Test
    fun mailSendCanBeInvokedWithoutTextBodyAndNotHtmlBody() {
        val cmd = EmailCmd()
        cmd.subject = "Hola"
        cmd.recipient = "delamos@micronaut.example"
        cmd.textBody = "Hello"

        val request: HttpRequest<EmailCmd> = HttpRequest.POST("/mail/send", cmd) // <3>
        val rsp: HttpResponse<*> = client.toBlocking().exchange<EmailCmd, Any>(request)

        Assertions.assertEquals(HttpStatus.OK, rsp.status)
    }

    @Test
    fun mailSendCanBeInvokedWithoutHtmlBodyAndNotTextBody() {
        val cmd = EmailCmd()
        cmd.subject = "Hola"
        cmd.recipient = "delamos@micronaut.example"
        cmd.htmlBody = "<h1>Hello</h1>"

        val request: HttpRequest<EmailCmd> = HttpRequest.POST("/mail/send", cmd) // <3>
        val rsp: HttpResponse<*> = client.toBlocking().exchange<EmailCmd, Any>(request)

        Assertions.assertEquals(HttpStatus.OK, rsp.status)
    }
}