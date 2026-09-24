from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.annotation import Get
from org.reactivestreams import Publisher
from reactor.core.publisher import Flux

from .book_catalogue_operations import BookCatalogueOperations
from .book_inventory_operations import BookInventoryOperations
from .book_recommendation import BookRecommendation

book_catalogue_operations: Annotated[BookCatalogueOperations, Inject]
book_inventory_operations: Annotated[BookInventoryOperations, Inject]


@Get("/books")
def index() -> Publisher[BookRecommendation]:
    return Flux.from_(book_catalogue_operations.findAll()).flatMap(
        lambda book: Flux.from_(book_inventory_operations.stock(book.isbn))
        .filter(lambda in_stock: bool(in_stock))
        .map(lambda _: book)
    ).map(lambda book: BookRecommendation(book.name))
