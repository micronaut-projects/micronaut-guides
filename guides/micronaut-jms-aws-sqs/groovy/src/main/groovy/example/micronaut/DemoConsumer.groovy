package example.micronaut

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.jms.annotations.JMSListener
import io.micronaut.jms.annotations.Queue
import io.micronaut.messaging.annotation.MessageBody

import static io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME

@CompileStatic
@Slf4j('LOGGER')
@JMSListener(CONNECTION_FACTORY_BEAN_NAME)  // <1>
class DemoConsumer {

    @Queue(value = "demo_queue", concurrency = "1-3")  // <2>
    void receive(@MessageBody String body) {  // <3>
        LOGGER.info("Message consumed: {}", body);
    }
}
