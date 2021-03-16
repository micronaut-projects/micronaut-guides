package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest // <1>
@Property(name = "spec.name", value = "mailcontroller") // <2>
class MailControllerValidationTest {

    @Inject
    ApplicationContext applicationContext;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void mailSendCannotBeInvokedWithoubSubject() {
        EmailCmd cmd = new EmailCmd();
        cmd.setRecipient("delamos@micronaut.example");
        cmd.setTextBody("Hola hola");
        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd); // <3>

        Executable e = () -> client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    public void mailSendCannotBeInvokedWithoutRecipient() {
        EmailCmd cmd = new EmailCmd();
        cmd.setSubject("Hola");
        cmd.setTextBody("Hola hola");
        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd); // <3>

        Executable e = () -> client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    public void mailSendCannotBeInvokedWithoutEitherTextBodyOrHtmlBody() {
        EmailCmd cmd = new EmailCmd();
        cmd.setSubject("Hola");
        cmd.setRecipient("delamos@micronaut.example");
        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd); // <3>

        Executable e = () -> client.toBlocking().exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    public void mailSendCanBeInvokedWithoutTextBodyAndNotHtmlBody() {
        EmailCmd cmd = new EmailCmd();
        cmd.setSubject("Hola");
        cmd.setRecipient("delamos@micronaut.example");
        cmd.setTextBody("Hello");

        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd); // <3>
        HttpResponse<?> rsp = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, rsp.getStatus());
    }

    @Test
    public void mailSendCanBeInvokedWithoutHtmlBodyAndNotTextBody() {
        EmailCmd cmd = new EmailCmd();
        cmd.setSubject("Hola");
        cmd.setRecipient("delamos@micronaut.example");
        cmd.setHtmlBody("<h1>Hello</h1>");

        HttpRequest<EmailCmd> request = HttpRequest.POST("/mail/send", cmd); // <3>
        HttpResponse<?> rsp = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, rsp.getStatus());
    }
}