package example.micronaut;

import io.micronaut.jms.annotations.JMSProducer;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.MessageBody;

import static io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME;

@JMSProducer(CONNECTION_FACTORY_BEAN_NAME) // <1>
public interface DemoProducer {

    @Queue("demo_queue")   // <2>
    void send(@MessageBody String body);  // <3>
}
