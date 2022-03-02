package example.micronaut;

import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SesRequest;
import software.amazon.awssdk.services.ses.model.SesResponse;

import static io.micronaut.email.BodyType.HTML;
import static io.micronaut.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Controller("/mail") // <1>
public class MailController {

    private static final Logger LOG = LoggerFactory.getLogger(MailController.class);

    private final AsyncEmailSender<SesRequest, SesResponse> emailSender;

    public MailController(AsyncEmailSender<SesRequest, SesResponse> emailSender) { // <2>
        this.emailSender = emailSender;
    }

    @Post("/send") // <3>
    public Publisher<HttpResponse<?>> send(@Body("to") String to) { // <4>
        return Mono.from(emailSender.sendAsync(Email.builder()
                        .to(to)
                        .subject("Sending email with Amazon SES is Fun")
                        .body("and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>", HTML)))
                .doOnNext(rsp -> {
                    if (rsp instanceof SendEmailResponse) {
                        LOG.info("message id: {}", ((SendEmailResponse) rsp).messageId());
                    }
                }).onErrorMap(EmailException.class, t -> new HttpStatusException(UNPROCESSABLE_ENTITY, "Email could not be sent"))
                .map(rsp -> HttpResponse.accepted()); // <5>
    }
}
