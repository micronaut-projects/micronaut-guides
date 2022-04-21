package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.email.Email
import io.micronaut.email.TransactionalEmailSender
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.function.Consumer
import javax.mail.Message
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Requires(property = "spec.name", value = "EmailControllerTest") // <1>
@Singleton
@Replaces(TransactionalEmailSender::class)
@Named(EmailSenderReplacement.NAME)
class EmailSenderReplacement : TransactionalEmailSender<Message, Unit> {

    val emails: MutableList<Email> = mutableListOf()

    override fun getName(): String = NAME

    override fun send(email: @Valid Email,
                      emailRequest: @NotNull Consumer<Message>) {
        emails.add(email)
    }

    companion object {
        const val NAME = "javaxmail"
    }
}
