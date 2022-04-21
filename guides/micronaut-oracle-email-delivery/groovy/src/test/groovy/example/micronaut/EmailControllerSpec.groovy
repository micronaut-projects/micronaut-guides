package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.email.Attachment
import io.micronaut.email.Email
import io.micronaut.email.TransactionalEmailSender
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.multipart.MultipartBody
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.email.BodyType.HTML
import static io.micronaut.email.BodyType.TEXT
import static io.micronaut.http.HttpStatus.OK
import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA_TYPE
import static io.micronaut.http.MediaType.TEXT_CSV
import static io.micronaut.http.MediaType.TEXT_CSV_TYPE
import static io.micronaut.http.MediaType.TEXT_PLAIN_TYPE
import static java.nio.charset.StandardCharsets.UTF_8

@Property(name = 'spec.name', value = 'EmailControllerTest')
@MicronautTest
class EmailControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    @Inject
    BeanContext beanContext

    private EmailSenderReplacement sender

    void setup() {
        TransactionalEmailSender<?, ?> sender = beanContext.getBean(TransactionalEmailSender)
        assert sender instanceof EmailSenderReplacement
        this.sender = (EmailSenderReplacement) sender
        this.sender.emails.clear()
    }

    void testBasic() {

        when:
        HttpResponse<?> response = client.toBlocking().exchange(
                HttpRequest.POST('/email/basic', null))

        then:
        response.status() == OK

        and:
        1 == sender.emails.size()

        when:
        Email email = sender.emails[0]

        then:
        'test@test.com' == email.from.email

        email.replyTo == null

        1 == email.to.size()
        'basic@domain.com' == email.to[0].email
        email.to[0].name == null

        email.cc == null

        email.bcc == null

        email.subject.startsWith('Micronaut Email Basic Test: ')

        email.attachments == null

        when:
        Optional<String> body = email.body.get(TEXT)

        then:
        'Basic email' == body.orElseThrow()
    }

    void testTemplate() {

        when:
        HttpResponse<?> response = client.toBlocking().exchange(
                HttpRequest.POST('/email/template/testingtesting', null))

        then:
        response.status() == OK

        and:
        1 == sender.emails.size()

        when:
        Email email = sender.emails[0]

        then:
        'test@test.com' == email.from.email

        email.replyTo == null

        1 == email.to.size()
        'template@domain.com' == email.to[0].email
        email.to[0].name == null

        email.cc == null

        email.bcc == null

        email.subject.startsWith('Micronaut Email Template Test: ')

        email.attachments == null

        Optional<String> body = email.body.get(HTML)
        body.orElseThrow().contains('Hello, <span>testingtesting</span>!')
    }

    void testAttachment() {

        when:
        HttpResponse<?> response = client.toBlocking().exchange(
                HttpRequest.POST('/email/attachment', MultipartBody.builder()
                        .addPart('file', 'test.csv', TEXT_CSV_TYPE, 'test,email'.getBytes(UTF_8))
                        .build())
                        .contentType(MULTIPART_FORM_DATA_TYPE)
                        .accept(TEXT_PLAIN_TYPE),
                String)

        then:
        response.status() == OK

        and:
        1 == sender.emails.size()

        when:
        Email email = sender.emails[0]

        then:
        'test@test.com' == email.from.email

        email.replyTo == null

        1 == email.to.size()
        'attachment@domain.com' == email.to[0].email
        email.to[0].name == null

        email.cc == null

        email.bcc == null

        email.subject.startsWith('Micronaut Email Attachment Test: ')

        1 == email.attachments.size()

        when:
        Attachment attachment = email.attachments[0]

        then:
        'test.csv' == attachment.filename
        TEXT_CSV == attachment.contentType
        'test,email' == new String(attachment.content)

        when:
        Optional<String> body = email.body.get(TEXT)

        then:
        'Attachment email' == body.orElseThrow()
    }
}
