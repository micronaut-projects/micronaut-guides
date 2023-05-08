package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.email.AsyncTransactionalEmailSender
import io.micronaut.email.Email
import io.micronaut.email.EmailException
import io.micronaut.email.ses.AsyncSesEmailSender
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.ses.model.SendEmailResponse
import software.amazon.awssdk.services.ses.model.SesRequest
import software.amazon.awssdk.services.ses.model.SesResponse
import java.util.function.Consumer
import jakarta.validation.Valid

@Requires(property = "spec.name", value = "MailControllerTest") // <1>
@Singleton
@Replaces(AsyncSesEmailSender::class)
@Named(AsyncSesEmailSender.NAME)
open class EmailSenderReplacement : AsyncTransactionalEmailSender<SesRequest, SesResponse> {

    val emails = mutableListOf<Email>()

    override fun getName(): String = AsyncSesEmailSender.NAME

    @Throws(EmailException::class)
    override fun sendAsync(@Valid email: Email,
                           emailRequest: Consumer<SesRequest>): Publisher<SesResponse> {
        emails.add(email)
        return Mono.just(SendEmailResponse.builder().messageId("xxx-yyy-zzz").build())
    }
}
