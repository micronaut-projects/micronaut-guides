from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.jms.annotations import JMSProducer, Queue
from micronaut.messaging.annotation import MessageBody


CONNECTION_FACTORY_BEAN_NAME = "sqsJmsConnectionFactory"


@JMSProducer(CONNECTION_FACTORY_BEAN_NAME)  # <1>
class DemoProducer(ABC):

    @Queue("demo_queue")  # <2>
    @abstractmethod
    def send(self, body: Annotated[str, MessageBody]) -> None:  # <3>
        ...
