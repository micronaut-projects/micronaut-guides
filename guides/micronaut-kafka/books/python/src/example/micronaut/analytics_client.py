from abc import ABC, abstractmethod

from micronaut.configuration.kafka.annotation import KafkaClient, Topic

from .book import Book


@KafkaClient
class AnalyticsClient(ABC):

    @Topic("analytics")  # <1>
    @abstractmethod
    def update_analytics(self, book: Book) -> None:  # <2>
        ...
