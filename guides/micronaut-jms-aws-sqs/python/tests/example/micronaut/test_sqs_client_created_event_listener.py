import java

from jakarta.inject import Singleton
from micronaut.context.event import BeanCreatedEvent, BeanCreatedEventListener


CreateQueueRequest = java.type("software.amazon.awssdk.services.sqs.model.CreateQueueRequest")
SqsClient = java.type("software.amazon.awssdk.services.sqs.SqsClient")


@Singleton  # <1>
class SqsClientCreatedEventListener(BeanCreatedEventListener[SqsClient]):  # <2>
    QUEUE_NAME = "demo_queue"

    def onCreated(self, event: BeanCreatedEvent[SqsClient]) -> SqsClient:
        client = event.getBean()
        if not any(self.QUEUE_NAME in str(url) for url in client.listQueues().queueUrls()):
            client.createQueue(
                CreateQueueRequest.builder()
                .queueName(self.QUEUE_NAME)
                .build()
            )
        return client
