package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.email.AsyncTransactionalEmailSender
import io.micronaut.email.Email
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.email.BodyType.HTML
import static io.micronaut.http.HttpStatus.ACCEPTED

@Property(name = 'spec.name', value = 'MailControllerSpec') // <1>
@MicronautTest // <2>
class MailControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient httpClient // <3>

    @Inject
    BeanContext beanContext

    void getMailSendEndpointSendsAnEmail() {

        when:
        HttpResponse<?> response = httpClient.toBlocking().exchange(
                HttpRequest.POST('/mail/send', [to: 'johnsnow@micronaut.example']))

        then:
        ACCEPTED == response.status()

        when:
        AsyncTransactionalEmailSender<?, ?> sender = beanContext.getBean(AsyncTransactionalEmailSender)

        then:
        sender instanceof EmailSenderReplacement

        when:
        EmailSenderReplacement sendgridSender = (EmailSenderReplacement) sender

        then:
        sendgridSender.emails
        1 == sendgridSender.emails.size()

        when:
        Email email = sendgridSender.emails[0]

        then:
        email.from.email == 'john@micronaut.example'
        null != email.to
        email.to.first()
        email.to.first().email == 'johnsnow@micronaut.example'
        email.subject == 'Sending email with Amazon SES is Fun'
        email.body
        email.body.get(HTML).present
        email.body.get(HTML).get() == 'and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>'
    }
}
