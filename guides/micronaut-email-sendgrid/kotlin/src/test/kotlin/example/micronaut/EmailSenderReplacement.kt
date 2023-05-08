package example.micronaut

import com.sendgrid.Request
import com.sendgrid.Response
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.email.AsyncEmailSender
import io.micronaut.email.AsyncTransactionalEmailSender
import io.micronaut.email.Email
import io.micronaut.email.EmailException
import io.micronaut.http.HttpStatus.ACCEPTED
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import java.util.function.Consumer
import jakarta.validation.Valid

@Requires(property = "spec.name", value = "MailControllerTest") // <1>
@Singleton
@Replaces(AsyncEmailSender::class)
@Named(EmailSenderReplacement.NAME)
open class EmailSenderReplacement : AsyncTransactionalEmailSender<Request, Response> {

    val emails = mutableListOf<Email>()

    override fun getName(): String = NAME

    @Throws(EmailException::class)
    override fun sendAsync(@Valid email: Email,
                           emailRequest: Consumer<Request>): Publisher<Response> {
        emails.add(email)
        val response = Response()
        response.statusCode = ACCEPTED.code
        return Mono.just(response)
    }

    companion object {
        const val NAME = "sendgrid"
    }
}
