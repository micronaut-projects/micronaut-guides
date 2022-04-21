package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.TransactionalEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import javax.mail.Message;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Requires(property = "spec.name", value = "EmailControllerTest") // <1>
@Singleton
@Replaces(TransactionalEmailSender.class)
@Named(EmailSenderReplacement.NAME)
class EmailSenderReplacement implements TransactionalEmailSender<Message, Void> {

    public static final String NAME = "javaxmail";

    final List<Email> emails = new ArrayList<>();

    @Override
    public String getName() {
        return NAME;
    }

    public List<Email> getEmails() {
        return emails;
    }

    @Override
    public Void send(@NotNull @Valid Email email,
                     @NotNull Consumer<Message> emailRequest) throws EmailException {
        emails.add(email);
        return null;
    }
}
