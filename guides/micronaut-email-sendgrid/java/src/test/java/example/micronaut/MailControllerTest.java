package example.micronaut;

import com.sendgrid.Request;
import com.sendgrid.Response;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

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
        assertTrue(sender instanceof SendgridEmailSenderReplacement);

        SendgridEmailSenderReplacement sendgridSender = (SendgridEmailSenderReplacement) sender;
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

    @Requires(property = "spec.name", value = "MailControllerTest") // <1>
    @Singleton
    @Replaces(AsyncEmailSender.class)
    @Named(SendgridEmailSenderReplacement.NAME)
    static class SendgridEmailSenderReplacement implements AsyncTransactionalEmailSender<Request, Response> {

        public static final String NAME = "sendgrid";

        private final List<Email> emails = new ArrayList<>();

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public Publisher<Response> sendAsync(@NotNull @Valid Email email,
                                             @NotNull Consumer<Request> emailRequest) throws EmailException {
            emails.add(email);
            Response response = new Response();
            response.setStatusCode(ACCEPTED.getCode());
            return Mono.just(response);
        }

        public List<Email> getEmails() {
            return emails;
        }
    }
}
