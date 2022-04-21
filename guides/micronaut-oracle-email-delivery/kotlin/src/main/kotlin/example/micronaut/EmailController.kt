package example.micronaut

import io.micronaut.email.Attachment
import io.micronaut.email.BodyType.HTML
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.template.TemplateBody
import io.micronaut.http.MediaType.APPLICATION_OCTET_STREAM_TYPE
import io.micronaut.http.MediaType.MULTIPART_FORM_DATA
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.views.ModelAndView
import java.io.IOException
import java.time.LocalDateTime

@Controller("/email")
class EmailController(private val emailSender: EmailSender<*, *>) {

    @Post(uri = "/basic", produces = [TEXT_PLAIN])
    fun index(): String {
        emailSender.send(
            Email.builder()
                .to("basic@domain.com")
                .subject("Micronaut Email Basic Test: " + LocalDateTime.now())
                .body("Basic email")
        )
        return "Email sent."
    }

    @Post(uri = "/template/{name}", produces = [TEXT_PLAIN])
    fun template(name: String): String {
        emailSender.send(
            Email.builder()
                .to("template@domain.com")
                .subject("Micronaut Email Template Test: " + LocalDateTime.now())
                .body(TemplateBody(HTML, ModelAndView("email", mapOf("name" to name))))
        )
        return "Email sent."
    }

    @Post(uri = "/attachment", produces = [TEXT_PLAIN], consumes = [MULTIPART_FORM_DATA])
    @Throws(IOException::class)
    fun attachment(file: CompletedFileUpload): String {
        emailSender.send(
            Email.builder()
                .to("attachment@domain.com")
                .subject("Micronaut Email Attachment Test: " + LocalDateTime.now())
                .body("Attachment email")
                .attachment(
                    Attachment.builder()
                        .filename(file.filename)
                        .contentType(file.contentType.orElse(APPLICATION_OCTET_STREAM_TYPE).toString())
                        .content(file.bytes)
                        .build())
        )
        return "Email sent."
    }
}
