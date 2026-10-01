from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from micronaut.retry.annotation import Fallback

from example.micronaut.book import Book
from example.micronaut.book_catalogue_operations import BookCatalogueOperations


@Requires(env="test")
@Fallback
@Singleton
class BookCatalogueClientStub(BookCatalogueOperations):

    async def find_all(self) -> list[Book]:
        return [
            Book("1491950358", "Building Microservices"),
            Book("1680502395", "Release It!"),
            Book("0321601912", "Continuous Delivery"),
        ]
