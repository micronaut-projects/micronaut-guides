from abc import ABC, abstractmethod

from micronaut.rabbitmq.annotation import Binding, RabbitClient

from .book import Book


@RabbitClient("micronaut")  # <1>
class AnalyticsClient(ABC):

    @Binding("analytics")  # <2>
    @abstractmethod
    def update_analytics(self, book: Book) -> None:  # <3>
        ...
