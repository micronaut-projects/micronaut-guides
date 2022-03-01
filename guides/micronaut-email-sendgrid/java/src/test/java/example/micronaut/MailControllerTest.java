package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.micronaut.email.BodyType.HTML;
import static io.micronaut.http.HttpStatus.ACCEPTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "MailControllerTest") // <1>
@MicronautTest // <2>
public class MailControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient; // <3>

    @Inject
    BeanContext beanContext;

    @Test
    void getMailSendEndpointSendsAnEmail() {

        HttpResponse<?> response = httpClient.toBlocking().exchange(
                HttpRequest.POST("/mail/send",
                Collections.singletonMap("to", "johnsnow@micronaut.example")));
        assertEquals(ACCEPTED, response.status());

        AsyncTransactionalEmailSender<?, ?> sender = beanContext.getBean(AsyncTransactionalEmailSender.class);
        assertTrue(sender instanceof EmailSenderReplacement);

        EmailSenderReplacement sendgridSender = (EmailSenderReplacement) sender;
        assertTrue(CollectionUtils.isNotEmpty(sendgridSender.emails));
        assertEquals(1, sendgridSender.emails.size());

        Email email = sendgridSender.emails.get(0);
        assertEquals(email.getFrom().getEmail(), "john@micronaut.example");
        assertNotNull(email.getTo());
        assertTrue(email.getTo().stream().findFirst().isPresent());
        assertEquals(email.getTo().stream().findFirst().get().getEmail(), "johnsnow@micronaut.example");
        assertEquals(email.getSubject(), "Sending email with Twilio Sendgrid is Fun");
        assertNotNull(email.getBody());
        assertTrue(email.getBody().get(HTML).isPresent());
        assertEquals(email.getBody().get(HTML).get(), "and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>");
    }
}
