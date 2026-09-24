import logging
from typing import Annotated

from java.util.concurrent.atomic import AtomicInteger
from micronaut.jms.annotations import JMSListener, Queue
from micronaut.messaging.annotation import MessageBody

from .demo_producer import CONNECTION_FACTORY_BEAN_NAME


LOG = logging.getLogger(__name__)


@JMSListener(CONNECTION_FACTORY_BEAN_NAME)  # <1>
class DemoConsumer:

    def __init__(self):
        self._message_count = AtomicInteger(0)

    @Queue(value="demo_queue")  # <2>
    def receive(self, body: Annotated[str, MessageBody]) -> None:  # <3>
        LOG.info("Message has been consumed. Message body: %s", body)
        self._message_count.incrementAndGet()

    @property
    def message_count(self) -> int:
        return self._message_count.intValue()
