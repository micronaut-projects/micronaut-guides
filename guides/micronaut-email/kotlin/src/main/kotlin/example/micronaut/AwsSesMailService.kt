package example.micronaut

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.Body
import software.amazon.awssdk.services.ses.model.Content
import software.amazon.awssdk.services.ses.model.Destination
import software.amazon.awssdk.services.ses.model.Message
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import software.amazon.awssdk.services.ses.model.SendEmailResponse
import javax.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Singleton // <1>
@Requires(condition = AwsResourceAccessCondition::class) // <2>
@Primary // <3>
class AwsSesMailService(
    @Value("\${AWS_REGION}") awsRegionEnv: String?,  // <4>
    @Value("\${AWS_SOURCE_EMAIL}") sourceEmailEnv: String?,
    @Value("\${aws.region}") awsRegionProp: String?,
    @Value("\${aws.sourceemail}") sourceEmailProp: String?,
) : EmailService {

    val sourceEmail = sourceEmailEnv ?: sourceEmailProp
    val awsRegion = awsRegionEnv ?: awsRegionProp
    val ses = SesClient.builder().region(software.amazon.awssdk.regions.Region.of(awsRegion)).build()

    override fun send(email: @NotNull @Valid Email) {
        val sendEmailRequest: SendEmailRequest = SendEmailRequest.builder()
            .destination(Destination.builder().toAddresses(email.replyTo()).build())
            .source(sourceEmail)
            .message(
                Message.builder().subject(Content.builder().data(email.subject()).build())
                    .body(Body.builder().text(Content.builder().data(email.textBody()).build()).build()).build()
            )
            .build()
        val response: SendEmailResponse = ses.sendEmail(sendEmailRequest)
        if (LOG.isInfoEnabled) {
            LOG.info("Sent email with id: {}", response.messageId())
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AwsSesMailService::class.java)
    }
}
