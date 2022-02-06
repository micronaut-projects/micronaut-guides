package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.ses.AsyncSesEmailSender;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SesRequest;
import software.amazon.awssdk.services.ses.model.SesResponse;

@Property(name = "spec.name", value="MailControllerTest") // <1>
@MicronautTest // <2>
public class MailControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient; // <3>

    @Inject
    BeanContext beanContext;

    @Test
    void getMailSendEndpointSendsAnEmail() {
        HttpResponse<?> response = httpClient.toBlocking().exchange(HttpRequest.POST("/mail/send",
                Collections.singletonMap("to", "johnsnow@micronaut.example")));
        assertEquals(HttpStatus.ACCEPTED, response.status());

        AsyncTransactionalEmailSender<?,?> sender = beanContext.getBean(AsyncTransactionalEmailSender.class);
        assertTrue(sender instanceof AsyncSesEmailSenderReplacement);
        AsyncSesEmailSenderReplacement sendgridSender = (AsyncSesEmailSenderReplacement) sender;
        assertTrue(CollectionUtils.isNotEmpty(sendgridSender.getEmails()));
        assertEquals(1, sendgridSender.getEmails().size());
        Email email = sendgridSender.getEmails().get(0);
        assertEquals(email.getFrom().getEmail(), "john@micronaut.example");
        assertNotNull(email.getTo());
        assertTrue(email.getTo().stream().findFirst().isPresent());
        assertEquals(email.getTo().stream().findFirst().get().getEmail(), "johnsnow@micronaut.example");
        assertEquals(email.getSubject(), "Sending email with Amazon SES is Fun");
        assertNotNull(email.getBody());
        assertTrue(email.getBody().get(BodyType.HTML).isPresent());
        assertEquals(email.getBody().get(BodyType.HTML).get(), "and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>");
    }

    @Requires(property = "spec.name", value="MailControllerTest") // <1>
    @Singleton
    @Replaces(AsyncSesEmailSender.class)
    @Named(AsyncSesEmailSender.NAME)
    static class AsyncSesEmailSenderReplacement implements AsyncTransactionalEmailSender<SesRequest, SesResponse> {
        private final List<Email> emails = new ArrayList<>();

        @Override
        public String getName() {
            return AsyncSesEmailSender.NAME;
        }

        @Override
        public Publisher<SesResponse> sendAsync(@NotNull @Valid Email email, @NotNull Consumer<SesRequest> emailRequest) throws EmailException {
            emails.add(email);
            return Mono.just(SendEmailResponse.builder().messageId("xxx-yyy-zzz").build());
        }

        public List<Email> getEmails() {
            return emails;
        }
    }
}
