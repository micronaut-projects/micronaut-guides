from abc import ABC, abstractmethod

from micronaut.rabbitmq.annotation import Binding, RabbitClient, RabbitProperty
from org.reactivestreams import Publisher


@RabbitClient("micronaut")  # <1>
@RabbitProperty(name="replyTo", value="amq.rabbitmq.reply-to")  # <2>
class InventoryClient(ABC):

    @Binding("books.inventory")  # <3>
    @abstractmethod
    def stock(self, isbn: str) -> Publisher[bool]:  # <4>
        ...
