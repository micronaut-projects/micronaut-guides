package example.micronaut

import io.micronaut.jms.annotations.JMSListener
import io.micronaut.jms.sqs.configuration.SqsConfiguration
import io.micronaut.messaging.annotation.MessageBody
import io.micronaut.jms.annotations.Queue
import org.slf4j.LoggerFactory

import io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME

@JMSListener(CONNECTION_FACTORY_BEAN_NAME)  // <1>
class DemoConsumer {

    @Queue(value = "demo_queue", concurrency = "1-3")  // <2>
    fun receive(@MessageBody body: String) {  // <3>
        LOGGER.info("Message consumed: {}", body)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DemoConsumer::class.java)
    }
}