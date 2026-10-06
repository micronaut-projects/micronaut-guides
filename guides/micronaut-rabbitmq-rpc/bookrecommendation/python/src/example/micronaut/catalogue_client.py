from abc import ABC, abstractmethod

from micronaut.rabbitmq.annotation import Binding, RabbitClient, RabbitProperty
from org.reactivestreams import Publisher

from .book import Book


@RabbitClient("micronaut")  # <1>
@RabbitProperty(name="replyTo", value="amq.rabbitmq.reply-to")  # <2>
class CatalogueClient(ABC):

    @Binding("books.catalogue")  # <3>
    @abstractmethod
    def find_all(self, data: bytes) -> Publisher[list[Book]]:  # <4>
        ...
