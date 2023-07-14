package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.ses.AsyncSesEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SesRequest;
import software.amazon.awssdk.services.ses.model.SesResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Requires(property = "spec.name", value = "MailControllerTest") // <1>
@Singleton
@Replaces(AsyncSesEmailSender.class)
@Named(AsyncSesEmailSender.NAME)
class EmailSenderReplacement implements AsyncTransactionalEmailSender<SesRequest, SesResponse> {

    private final List<Email> emails = new ArrayList<>();

    @Override
    public String getName() {
        return AsyncSesEmailSender.NAME;
    }

    @Override
    public Publisher<SesResponse> sendAsync(@NotNull @Valid Email email,
                                            @NotNull Consumer<SesRequest> emailRequest) throws EmailException {
        emails.add(email);
        return Mono.just(SendEmailResponse.builder().messageId("xxx-yyy-zzz").build());
    }

    public List<Email> getEmails() {
        return emails;
    }
}
