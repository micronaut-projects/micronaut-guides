from jakarta.inject import Singleton
from jakarta.transaction import Transactional
from micronaut.context.event import StartupEvent
from micronaut.runtime.event.annotation import EventListener

from .book import Book
from .book_repository import BookRepository


@Singleton  # <1>
class DataPopulator:
    def __init__(self, book_repository: BookRepository):  # <2>
        self.book_repository = book_repository

    @EventListener  # <3>
    @Transactional  # <4>
    def init(self, event: StartupEvent) -> None:
        if self.book_repository.count() == 0:
            self.book_repository.save(Book("1491950358", "Building Microservices"))
            self.book_repository.save(Book("1680502395", "Release It!"))
            self.book_repository.save(Book("0321601912", "Continuous Delivery"))
