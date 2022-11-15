package example.micronaut

import io.micronaut.jms.annotations.JMSListener
import io.micronaut.jms.annotations.Queue
import io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME
import io.micronaut.messaging.annotation.MessageBody
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

@JMSListener(CONNECTION_FACTORY_BEAN_NAME) // <1>
class DemoConsumer {
    private val messageCount = AtomicInteger(0)

    @Queue(value = "demo_queue", concurrency = "1-3") // <2>
    fun receive(@MessageBody body: String?) {  // <3>
        LOGGER.info("Message has been consumed. Message body: {}", body)
        messageCount.incrementAndGet()
    }

    fun getMessageCount(): Int {
        return messageCount.toInt()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DemoConsumer::class.java)
    }
}