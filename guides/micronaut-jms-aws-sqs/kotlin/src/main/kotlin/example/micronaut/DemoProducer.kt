package example.micronaut

import io.micronaut.jms.annotations.JMSProducer
import io.micronaut.jms.annotations.Queue
import io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME
import io.micronaut.messaging.annotation.MessageBody

@JMSProducer(CONNECTION_FACTORY_BEAN_NAME)  // <1>
interface DemoProducer {

    @Queue("demo_queue")  // <2>
    fun send(@MessageBody body: String?)  // <3>
}