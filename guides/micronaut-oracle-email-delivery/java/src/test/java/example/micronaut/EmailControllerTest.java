package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.email.Attachment;
import io.micronaut.email.Email;
import io.micronaut.email.TransactionalEmailSender;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.micronaut.email.BodyType.HTML;
import static io.micronaut.email.BodyType.TEXT;
import static io.micronaut.http.HttpStatus.OK;
import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA_TYPE;
import static io.micronaut.http.MediaType.TEXT_CSV;
import static io.micronaut.http.MediaType.TEXT_CSV_TYPE;
import static io.micronaut.http.MediaType.TEXT_PLAIN_TYPE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "EmailControllerTest") // <1>
@MicronautTest // <2>
class EmailControllerTest {

    @Inject
    @Client("/")
    HttpClient client; // <3>

    @Inject
    BeanContext beanContext;

    private EmailSenderReplacement sender;

    @BeforeEach
    void setup() {
        TransactionalEmailSender<?, ?> sender = beanContext.getBean(TransactionalEmailSender.class);
        assertTrue(sender instanceof EmailSenderReplacement);
        this.sender = (EmailSenderReplacement) sender;
        this.sender.getEmails().clear();
    }

    @Test
    void testBasic() {

        HttpResponse<?> response = client.toBlocking().exchange(
                HttpRequest.POST("/email/basic", null));
        assertEquals(response.status(), OK);

        assertEquals(1, sender.emails.size());
        Email email = sender.getEmails().get(0);

        assertEquals("test@test.com", email.getFrom().getEmail());

        assertNull(email.getReplyTo());

        assertNotNull(email.getTo());
        assertEquals(1, email.getTo().size());
        assertEquals("basic@domain.com", email.getTo().iterator().next().getEmail());
        assertNull(email.getTo().iterator().next().getName());

        assertNull(email.getCc());

        assertNull(email.getBcc());

        assertTrue(email.getSubject().startsWith("Micronaut Email Basic Test: "));

        assertNull(email.getAttachments());

        assertNotNull(email.getBody());
        Optional<String> body = email.getBody().get(TEXT);
        assertEquals("Basic email", body.orElseThrow());
    }

    @Test
    void testTemplate() {

        HttpResponse<?> response = client.toBlocking().exchange(
                HttpRequest.POST("/email/template/testingtesting", null));
        assertEquals(response.status(), OK);

        assertEquals(1, sender.emails.size());
        Email email = sender.getEmails().get(0);

        assertEquals("test@test.com", email.getFrom().getEmail());

        assertNull(email.getReplyTo());

        assertNotNull(email.getTo());
        assertEquals(1, email.getTo().size());
        assertEquals("template@domain.com", email.getTo().iterator().next().getEmail());
        assertNull(email.getTo().iterator().next().getName());

        assertNull(email.getCc());

        assertNull(email.getBcc());

        assertTrue(email.getSubject().startsWith("Micronaut Email Template Test: "));

        assertNull(email.getAttachments());

        assertNotNull(email.getBody());
        Optional<String> body = email.getBody().get(HTML);
        assertTrue(body.orElseThrow().contains("Hello, <span>testingtesting</span>!"));
    }

    @Test
    void testAttachment() {

        HttpResponse<?> response = client.toBlocking().exchange(
                HttpRequest.POST("/email/attachment", MultipartBody.builder()
                        .addPart("file", "test.csv", TEXT_CSV_TYPE, "test,email".getBytes(UTF_8))
                        .build())
                        .contentType(MULTIPART_FORM_DATA_TYPE)
                        .accept(TEXT_PLAIN_TYPE),
                String.class);
        assertEquals(response.status(), OK);

        assertEquals(1, sender.emails.size());
        Email email = sender.getEmails().get(0);

        assertEquals("test@test.com", email.getFrom().getEmail());

        assertNull(email.getReplyTo());

        assertNotNull(email.getTo());
        assertEquals(1, email.getTo().size());
        assertEquals("attachment@domain.com", email.getTo().iterator().next().getEmail());
        assertNull(email.getTo().iterator().next().getName());

        assertNull(email.getCc());

        assertNull(email.getBcc());

        assertTrue(email.getSubject().startsWith("Micronaut Email Attachment Test: "));

        assertNotNull(email.getAttachments());
        assertEquals(1, email.getAttachments().size());
        Attachment attachment = email.getAttachments().get(0);
        assertEquals("test.csv", attachment.getFilename());
        assertEquals(TEXT_CSV, attachment.getContentType());
        assertEquals("test,email", new String(attachment.getContent()));

        assertNotNull(email.getBody());
        Optional<String> body = email.getBody().get(TEXT);
        assertEquals("Attachment email", body.orElseThrow());
    }
}
