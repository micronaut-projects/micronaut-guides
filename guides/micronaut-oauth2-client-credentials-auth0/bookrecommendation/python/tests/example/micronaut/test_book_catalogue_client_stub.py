from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from micronaut.retry.annotation import Fallback
from org.reactivestreams import Publisher
from reactor.core.publisher import Flux

from example.micronaut.book import Book
from example.micronaut.book_catalogue_operations import BookCatalogueOperations


@Requires(env="test")
@Fallback
@Singleton
class BookCatalogueClientStub(BookCatalogueOperations):

    def findAll(self) -> Publisher[Book]:
        building_microservices = Book("1491950358", "Building Microservices")
        release_it = Book("1680502395", "Release It!")
        return Flux.just(building_microservices, release_it)
