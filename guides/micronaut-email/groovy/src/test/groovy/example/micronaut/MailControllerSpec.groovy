package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest // <1>
@Property(name = "spec.name", value = "mailcontroller") // <2>
class MailControllerSpec extends Specification {

    @Inject
    ApplicationContext applicationContext // <3>

    @Inject
    @Client("/")
    HttpClient client // <4>

    void "mail send interacts once email service"() {
        given:
        EmailCmd cmd = new EmailCmd(subject: "Test", recipient: "delamos@grails.example", textBody: "Hola hola")
        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd) // <5>

        when:
        EmailService emailService = applicationContext.getBean(EmailService)

        then:
        emailService instanceof MockEmailService

        when:
        HttpResponse<?> rsp = client.toBlocking().exchange(request)
        then:
        HttpStatus.OK == rsp.getStatus()
        old(((MockEmailService) emailService).emails.size()) + 1 == ((MockEmailService) emailService).emails.size() // <6>
    }
}
