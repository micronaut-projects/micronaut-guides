from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.annotation import Get
from org.reactivestreams import Publisher
from reactor.core.publisher import Flux

from .book_recommendation import BookRecommendation
from .catalogue_client import CatalogueClient
from .inventory_client import InventoryClient


catalogue_client: Annotated[CatalogueClient, Inject]  # <1>
inventory_client: Annotated[InventoryClient, Inject]  # <1>


@Get("/books")  # <2>
def index() -> Publisher[BookRecommendation]:
    return Flux.from_(catalogue_client.find_all(b"")).flatMap(
        lambda books: Flux.fromIterable(books)
    ).flatMap(
        lambda book: Flux.from_(inventory_client.stock(book.isbn))
        .filter(lambda in_stock: bool(in_stock))
        .map(lambda _: book)
    ).map(lambda book: BookRecommendation(book.name))
