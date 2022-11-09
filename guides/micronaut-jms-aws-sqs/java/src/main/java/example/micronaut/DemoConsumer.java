package example.micronaut;

import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME;

@JMSListener(CONNECTION_FACTORY_BEAN_NAME)
public class DemoConsumer {
    private static Logger LOGGER = LoggerFactory.getLogger(DemoConsumer.class);

    @Queue(value = "demo_queue", concurrency = "1-3")
    public void receive(@MessageBody String body) {
        LOGGER.info("Message consumed: {}", body);
    }
}
