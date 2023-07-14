package example.micronaut;

import io.micronaut.email.Attachment;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.TransactionalEmailSender;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import jakarta.mail.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

@MicronautTest // <1>
class EmailControllerTest {

    @Inject
    @Client("/")
    HttpClient client; // <2>

    List<Email> emails = new ArrayList<>();

    @AfterEach
    void cleanup() {
        emails.clear();
    }

    @Test
    void testBasic() {

        HttpResponse<?> response = client.toBlocking().exchange(
                HttpRequest.POST("/email/basic", null));
        assertEquals(response.status(), OK);

        assertEquals(1, emails.size());
        Email email = emails.get(0);

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
                HttpRequest.POST("/email/template/testing", null));
        assertEquals(response.status(), OK);

        assertEquals(1, emails.size());
        Email email = emails.get(0);

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
        assertTrue(body.orElseThrow().contains("Hello, <span>testing</span>!"));
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

        assertEquals(1, emails.size());
        Email email = emails.get(0);

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

    @MockBean(TransactionalEmailSender.class)
    @Named("mock")
    TransactionalEmailSender<Message, Void> mockSender() {
        return new TransactionalEmailSender<>() {

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public Void send(Email email, Consumer emailRequest) throws EmailException {
                emails.add(email);
                return null;
            }
        };
    }
}
