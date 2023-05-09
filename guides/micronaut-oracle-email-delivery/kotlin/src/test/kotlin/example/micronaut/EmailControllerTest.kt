package example.micronaut

import io.micronaut.email.BodyType
import io.micronaut.email.BodyType.TEXT
import io.micronaut.email.Email
import io.micronaut.email.TransactionalEmailSender
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.MediaType.MULTIPART_FORM_DATA_TYPE
import io.micronaut.http.MediaType.TEXT_CSV
import io.micronaut.http.MediaType.TEXT_CSV_TYPE
import io.micronaut.http.MediaType.TEXT_PLAIN_TYPE
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.multipart.MultipartBody
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Named
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets.UTF_8
import java.util.function.Consumer
import jakarta.mail.Message
import jakarta.validation.Valid

@MicronautTest // <1>
class EmailControllerTest(@Client("/") val client: HttpClient) { // <2>

    var emails: MutableList<Email> = mutableListOf()

    @AfterEach
    fun cleanup() {
        emails.clear()
    }

    @Test
    fun testBasic() {

        val response: HttpResponse<*> = client.toBlocking().exchange<Any, Any>(
            HttpRequest.POST("/email/basic", null)
        )
        assertEquals(response.status(), OK)

        assertEquals(1, emails.size)
        val email = emails[0]

        assertEquals("test@test.com", email.from.email)

        assertNull(email.replyTo)

        assertNotNull(email.to)
        assertEquals(1, email.to.size)
        assertEquals("basic@domain.com", email.to.iterator().next().email)
        assertNull(email.to.iterator().next().name)

        assertNull(email.cc)

        assertNull(email.bcc)

        assertTrue(email.subject.startsWith("Micronaut Email Basic Test: "))

        assertNull(email.attachments)

        assertNotNull(email.body)
        val body = email.body[TEXT]
        assertEquals("Basic email", body.orElseThrow())
    }

    @Test
    fun testTemplate() {

        val response: HttpResponse<*> = client.toBlocking().exchange<Any, Any>(
            HttpRequest.POST("/email/template/testingtesting", null)
        )
        assertEquals(response.status(), OK)

        assertEquals(1, emails.size)
        val email = emails[0]

        assertEquals("test@test.com", email.from.email)

        assertNull(email.replyTo)

        assertNotNull(email.to)
        assertEquals(1, email.to.size)
        assertEquals("template@domain.com", email.to.iterator().next().email)
        assertNull(email.to.iterator().next().name)

        assertNull(email.cc)

        assertNull(email.bcc)

        assertTrue(email.subject.startsWith("Micronaut Email Template Test: "))

        assertNull(email.attachments)

        assertNotNull(email.body)
        val body = email.body[BodyType.HTML]
        assertTrue(body.orElseThrow().contains("Hello, <span>testingtesting</span>!"))
    }

    @Test
    fun testAttachment() {

        val response: HttpResponse<*> = client.toBlocking().exchange(
            HttpRequest.POST("/email/attachment", MultipartBody.builder()
                .addPart("file", "test.csv", TEXT_CSV_TYPE, "test,email".toByteArray(UTF_8))
                .build())
                .contentType(MULTIPART_FORM_DATA_TYPE)
                .accept(TEXT_PLAIN_TYPE),
            String::class.java
        )
        assertEquals(response.status(), OK)

        assertEquals(1, emails.size)
        val email = emails[0]

        assertEquals("test@test.com", email.from.email)

        assertNull(email.replyTo)

        assertNotNull(email.to)
        assertEquals(1, email.to.size)
        assertEquals("attachment@domain.com", email.to.iterator().next().email)
        assertNull(email.to.iterator().next().name)

        assertNull(email.cc)

        assertNull(email.bcc)

        assertTrue(email.subject.startsWith("Micronaut Email Attachment Test: "))

        assertNotNull(email.attachments)
        assertEquals(1, email.attachments.size)
        val attachment = email.attachments[0]
        assertEquals("test.csv", attachment.filename)
        assertEquals(TEXT_CSV, attachment.contentType)
        assertEquals("test,email", String(attachment.content))

        assertNotNull(email.body)
        val body = email.body[TEXT]
        assertEquals("Attachment email", body.orElseThrow())
    }

    @MockBean(TransactionalEmailSender::class)
    @Named("mock")
    fun mockSender(): TransactionalEmailSender<Message, Unit> = object : TransactionalEmailSender<Message, Unit> {

        override fun getName() = "test"

        override fun send(@Valid email: Email, emailRequest: Consumer<Message>) {
            emails.add(email)
        }
    }
}
