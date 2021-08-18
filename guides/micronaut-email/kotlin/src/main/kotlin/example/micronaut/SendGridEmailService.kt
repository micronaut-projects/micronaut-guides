package example.micronaut

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Personalization
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import org.slf4j.LoggerFactory
import jakarta.inject.Singleton
import java.io.IOException
import javax.validation.Valid
import javax.validation.constraints.NotNull
import java.util.stream.Collectors

@Singleton // <1>
@Requires(condition = SendGridEmailCondition::class) // <2>
class SendGridEmailService(@Value("\${SENDGRID_APIKEY:none}") apiKeyEnv: String,  // <3>
                                    @Value("\${SENDGRID_FROM_EMAIL:none}") fromEmailEnv: String,
                                    @Value("\${sendgrid.apikey:none}") apiKeyProp: String,
                                    @Value("\${sendgrid.fromemail:none}") fromEmailProp: String) : EmailService {
    val apiKey: String
    val fromEmail: String

    init {
        this.apiKey = if (apiKeyEnv != "none") apiKeyEnv else apiKeyProp
        this.fromEmail = if (fromEmailEnv != "none") fromEmailEnv else fromEmailProp
    }
    protected fun contentOfEmail(email: Email): Content? {
        if (email.textBody() != null) {
            return Content("text/plain", email.textBody())
        }
        return if (email.htmlBody() != null) {
            Content("text/html", email.htmlBody())
        } else null
    }

    override fun send(email: @NotNull @Valid Email) {
        val personalization = Personalization()
        personalization.subject = email.subject()
        val to = com.sendgrid.helpers.mail.objects.Email(email.recipient())
        personalization.addTo(to)
        if (email.cc() != null) {
            for (cc in email.cc()!!) {
                val ccEmail = com.sendgrid.helpers.mail.objects.Email()
                ccEmail.email = cc
                personalization.addCc(ccEmail)
            }
        }
        if (email.bcc() != null) {
            for (bcc in email.bcc()!!) {
                val bccEmail = com.sendgrid.helpers.mail.objects.Email()
                bccEmail.email = bcc
                personalization.addBcc(bccEmail)
            }
        }
        val mail = Mail()
        val from = com.sendgrid.helpers.mail.objects.Email()
        from.email = fromEmail
        mail.from = from
        mail.addPersonalization(personalization)
        val content: Content? = contentOfEmail(email)
        mail.addContent(content)
        val sg = SendGrid(apiKey)
        val request = Request()
        try {
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()
            val response = sg.api(request)
            if (LOG.isInfoEnabled()) {
                LOG.info("Status Code: {}", response.statusCode)
                LOG.info("Body: {}", response.body)
                LOG.info("Headers {}", response.headers
                    .keySet()
                    .stream()
                    .map { key -> key.toString() + "=" + response.headers.get(key) }
                    .collect(Collectors.joining(", ", "{", "}")))
            }
        } catch (ex: IOException) {
            LOG.error(ex.message)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SendGridEmailService::class.java)
    }
}
