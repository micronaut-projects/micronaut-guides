package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.email.Email
import io.micronaut.email.EmailException
import io.micronaut.email.TransactionalEmailSender
import jakarta.inject.Named
import jakarta.inject.Singleton

import javax.mail.Message
import javax.validation.Valid
import javax.validation.constraints.NotNull
import java.util.function.Consumer

@Requires(property = 'spec.name', value = 'EmailControllerTest')
@Singleton
@Replaces(TransactionalEmailSender)
@Named(NAME)
class EmailSenderReplacement implements TransactionalEmailSender<Message, Void> {

    public static final String NAME = 'javaxmail'

    final List<Email> emails = []

    @Override
    String getName() { NAME }

    List<Email> getEmails() { emails }

    @Override
    Void send(@NotNull @Valid Email email,
              @NotNull Consumer<Message> emailRequest) throws EmailException {
        emails << email
        return null
    }
}
