package example.micronaut;

import io.micronaut.email.Attachment;
import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import io.micronaut.email.template.TemplateBody;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.views.ModelAndView;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.io.IOException;
import java.time.LocalDateTime;

import static io.micronaut.email.BodyType.HTML;
import static io.micronaut.http.MediaType.APPLICATION_OCTET_STREAM_TYPE;
import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA;
import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static java.util.Collections.singletonMap;

@ExecuteOn(TaskExecutors.IO)
@Controller("/email")
class EmailController {

    private final EmailSender<?, ?> emailSender;

    EmailController(EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender;
    }

    @Post(uri="/basic", produces = TEXT_PLAIN)
    String index() {
        emailSender.send(Email.builder()
                .to("basic@domain.com")
                .subject("Micronaut Email Basic Test: " + LocalDateTime.now())
                .body("Basic email"));
        return "Email sent.";
    }

    @Post(uri = "/template/{name}", produces = TEXT_PLAIN)
    String template(String name) {
        emailSender.send(Email.builder()
                .to("template@domain.com")
                .subject("Micronaut Email Template Test: " + LocalDateTime.now())
                .body(new TemplateBody<>(HTML,
                        new ModelAndView<>("email", singletonMap("name", name)))));
        return "Email sent.";
    }

    @Post(uri = "/attachment", produces = TEXT_PLAIN, consumes = MULTIPART_FORM_DATA)
    String attachment(CompletedFileUpload file) throws IOException {
        emailSender.send(Email.builder()
                .to("attachment@domain.com")
                .subject("Micronaut Email Attachment Test: " + LocalDateTime.now())
                .body("Attachment email")
                .attachment(Attachment.builder()
                        .filename(file.getFilename())
                        .contentType(file.getContentType().orElse(APPLICATION_OCTET_STREAM_TYPE).toString())
                        .content(file.getBytes())
                        .build()
                ));
        return "Email sent.";
    }
}
