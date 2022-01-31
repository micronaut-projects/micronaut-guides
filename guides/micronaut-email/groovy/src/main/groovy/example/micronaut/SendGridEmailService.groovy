package example.micronaut

import com.sendgrid.SendGrid
import com.sendgrid.Request
import com.sendgrid.Response
import com.sendgrid.Method
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Personalization
import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import io.micronaut.core.annotation.NonNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import jakarta.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotNull

@CompileStatic
@Singleton // <1>
@Requires(condition = SendGridEmailCondition) // <2>
class SendGridEmailService implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(SendGridEmailService)

    protected final String apiKey

    protected final String fromEmail

    SendGridEmailService(@Value('${SENDGRID_APIKEY:none}') String apiKeyEnv, // <3>
                         @Value('${SENDGRID_FROM_EMAIL:none}') String fromEmailEnv,                         @Value('${sendgrid.apikey:none}') String apiKeyProp,
                         @Value('${sendgrid.fromemail:none}') String fromEmailProp) {
        this.apiKey = apiKeyEnv != null && !apiKeyEnv.equals("none") ? apiKeyEnv : apiKeyProp
        this.fromEmail = fromEmailEnv != null && !fromEmailEnv.equals("none")  ? fromEmailEnv: fromEmailProp
    }

    protected static Content contentOfEmail(Email email) {
        if ( email.textBody !=null ) {
            return new Content("text/plain", email.textBody)
        }
        if ( email.htmlBody !=null ) {
            return new Content("text/html", email.htmlBody)
        }
        return null
    }

    @Override
    void send(@NonNull @NotNull @Valid Email email) {

        Personalization personalization = new Personalization()
        personalization.setSubject(email.subject)

        com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email(email.recipient)
        personalization.addTo(to)

        if ( email.cc != null ) {
            for ( String cc : email.cc ) {
                com.sendgrid.helpers.mail.objects.Email ccEmail = new com.sendgrid.helpers.mail.objects.Email()
                ccEmail.setEmail(cc)
                personalization.addCc(ccEmail)
            }
        }

        if ( email.bcc  != null ) {
            for ( String bcc : email.bcc ) {
                com.sendgrid.helpers.mail.objects.Email bccEmail = new com.sendgrid.helpers.mail.objects.Email()
                bccEmail.setEmail(bcc)
                personalization.addBcc(bccEmail)
            }
        }

        Mail mail = new Mail()
        com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email()
        from.setEmail(fromEmail)
        mail.from = from
        mail.addPersonalization(personalization)
        Content content = contentOfEmail(email)
        mail.addContent(content)

        SendGrid sg = new SendGrid(apiKey)
        Request request = new Request()
        try {
            request.setMethod(Method.POST)
            request.setEndpoint("mail/send")
            request.setBody(mail.build())

            Response response = sg.api(request)

            if (LOG.infoEnabled) {
                LOG.info("Status Code: {}", String.valueOf(response.statusCode))
                LOG.info("Body: {}", response.body)
                LOG.info("Headers {}", response.headers.collect { k, v -> "$k=$v" }.join(", "))
            }
        } catch (IOException e) {
            LOG.error(e.message, e)
        }
    }
}
