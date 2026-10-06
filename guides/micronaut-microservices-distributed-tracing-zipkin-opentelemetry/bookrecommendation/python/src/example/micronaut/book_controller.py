from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.annotation import Get

from .book_catalogue_operations import BookCatalogueOperations
from .book_inventory_operations import BookInventoryOperations
from .book_recommendation import BookRecommendation

book_catalogue_operations: Annotated[BookCatalogueOperations, Inject]
book_inventory_operations: Annotated[BookInventoryOperations, Inject]


@Get("/books")
async def index() -> list[BookRecommendation]:
    recommendations = []
    for book in await book_catalogue_operations.find_all():
        if await book_inventory_operations.stock(book.isbn):
            recommendations.append(BookRecommendation(book.name))
    return recommendations
