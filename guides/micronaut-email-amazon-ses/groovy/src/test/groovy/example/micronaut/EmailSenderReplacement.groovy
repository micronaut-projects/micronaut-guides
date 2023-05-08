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

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import java.util.function.Consumer

@Requires(property = 'spec.name', value = 'MailControllerSpec') // <1>
@Singleton
@Replaces(AsyncSesEmailSender)
@Named(AsyncSesEmailSender.NAME)
class EmailSenderReplacement implements AsyncTransactionalEmailSender<SesRequest, SesResponse> {

    final List<Email> emails = []

    @Override
    String getName() {
        AsyncSesEmailSender.NAME
    }

    @Override
    Publisher<SesResponse> sendAsync(@NotNull @Valid Email email,
                                     @NotNull Consumer<SesRequest> emailRequest) throws EmailException {
        emails << email
        Mono.just(SendEmailResponse.builder().messageId('xxx-yyy-zzz').build())
    }
}
