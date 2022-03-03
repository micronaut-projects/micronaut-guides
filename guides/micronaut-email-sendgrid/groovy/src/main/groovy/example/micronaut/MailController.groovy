package example.micronaut

import com.sendgrid.Request
import com.sendgrid.Response
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.email.AsyncEmailSender
import io.micronaut.email.Email
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

import static io.micronaut.email.BodyType.HTML

@CompileStatic
@Slf4j
@Controller('/mail') // <1>
class MailController {

    private final AsyncEmailSender<Request, Response> emailSender

    MailController(AsyncEmailSender<Request, Response> emailSender) { // <2>
        this.emailSender = emailSender
    }

    @Post('/send') // <3>
    Publisher<HttpResponse<?>> send(@Body('to') String to) { // <4>
        Mono.from(emailSender.sendAsync(Email.builder()
                .to(to)
                .subject('Sending email with Twilio Sendgrid is Fun')
                .body('and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>', HTML)))
                .doOnNext(rsp -> {
                    log.info('response status {}\nresponse body {}\nresponse headers {}',
                            rsp.statusCode, rsp.body, rsp.headers)
                }).map(rsp -> rsp.statusCode >= 400 ?
                HttpResponse.unprocessableEntity() :
                HttpResponse.accepted()) // <5>
                as Publisher
    }
}
