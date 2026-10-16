from micronaut.context.annotation import Requires
from micronaut.context.env import Environment
from micronaut.rabbitmq.annotation import Queue, RabbitListener

from .analytics_service import AnalyticsService
from .book import Book


@Requires(notEnv=Environment.TEST)  # <1>
@RabbitListener  # <2>
class AnalyticsListener:

    def __init__(self, analytics_service: AnalyticsService):  # <3>
        self.analytics_service = analytics_service

    @Queue("analytics")  # <4>
    def update_analytics(self, book: Book) -> None:
        self.analytics_service.update_book_analytics(book)  # <5>
