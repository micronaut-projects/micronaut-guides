package example.micronaut

import io.micronaut.jms.annotations.JMSListener
import io.micronaut.jms.sqs.configuration.SqsConfiguration
import io.micronaut.messaging.annotation.MessageBody
import com.example.DemoConsumer
import io.micronaut.jms.annotations.Queue
import org.slf4j.LoggerFactory

@JMSListener(SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME)
class DemoConsumer {

    @Queue(value = "demo_queue", concurrency = "1-3")
    fun receive(@MessageBody body: String?) {
        LOGGER.info("Message consumed: {}", body)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DemoConsumer::class.java)
    }
}