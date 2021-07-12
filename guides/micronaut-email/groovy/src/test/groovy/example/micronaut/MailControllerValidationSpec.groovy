package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import javax.inject.Inject

@MicronautTest // <1>
@Property(name = "spec.name", value = "mailcontroller") // <2>
class MailControllerValidationSpec extends Specification {

    @Inject
    ApplicationContext applicationContext;

    @Inject
    @Client("/")
    HttpClient client;

    void "mail send cannot be invoked without subject"() {
        given:
        EmailCmd cmd = new EmailCmd(recipient: "delamos@micronaut.example", textBody: "Hola hola")
        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd) // <3>

        when:
        client.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown()
        HttpStatus.BAD_REQUEST == e.status
    }

    void "mail send cannot be invoked without recipient"() {
        given:
        EmailCmd cmd = new EmailCmd(subject: "Hola", textBody: "Hola hola")
        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd) // <3>

        when:
        client.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown()
        HttpStatus.BAD_REQUEST == e.status
    }

    void "mail send cannot be invoked without either text body or html body"() {
        EmailCmd cmd = new EmailCmd(subject: "Hola", recipient: "delamos@micronaut.example")
        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd) // <3>

        when:
        client.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown()
        HttpStatus.BAD_REQUEST == e.status
    }

    void "mail send can be invoked without text body and not html body"() {
        given:
        EmailCmd cmd = new EmailCmd(subject: "Hola", recipient: "delamos@micronaut.example", textBody: "Hello")
        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd) // <3>

        when:
        HttpResponse<?> rsp = client.toBlocking().exchange(request)

        then:
        HttpStatus.OK == rsp.getStatus()
    }

    void "mail send can be invoked with html body and not text body"() {
        given:
        EmailCmd cmd = new EmailCmd(subject: "Hola", recipient: "delamos@micronaut.example", htmlBody: "<h1>Hello</h1>")
        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd) // <3>

        when:
        HttpResponse<?> rsp = client.toBlocking().exchange(request)

        then:
        HttpStatus.OK == rsp.getStatus()
    }
}