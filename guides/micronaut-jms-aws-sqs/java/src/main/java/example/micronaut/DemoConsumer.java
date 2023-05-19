package example.micronaut;

import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME;

@JMSListener(CONNECTION_FACTORY_BEAN_NAME)  // <1>
public class DemoConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoConsumer.class);

    private final AtomicInteger messageCount = new AtomicInteger(0);

    @Queue(value = "demo_queue", concurrency = "1-3")  // <2>
    public void receive(@MessageBody String body) {  // <3>
        LOGGER.info("Message has been consumed. Message body: {}", body);
        messageCount.incrementAndGet();
    }

    int getMessageCount() {
        return messageCount.intValue();
    }
}
