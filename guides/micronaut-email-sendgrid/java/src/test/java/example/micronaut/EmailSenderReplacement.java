package example.micronaut;

import com.sendgrid.Request;
import com.sendgrid.Response;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static io.micronaut.http.HttpStatus.ACCEPTED;

@Requires(property = "spec.name", value = "MailControllerTest") // <1>
@Singleton
@Replaces(AsyncEmailSender.class)
@Named(EmailSenderReplacement.NAME)
class EmailSenderReplacement implements AsyncTransactionalEmailSender<Request, Response> {

    public static final String NAME = "sendgrid";

    final List<Email> emails = new ArrayList<>();

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
