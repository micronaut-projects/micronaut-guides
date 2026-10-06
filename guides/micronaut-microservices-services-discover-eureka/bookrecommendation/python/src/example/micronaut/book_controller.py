import asyncio
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
    books = await book_catalogue_operations.find_all()
    in_stock = await asyncio.gather(*(book_inventory_operations.stock(book.isbn) for book in books))
    return [BookRecommendation(book.name) for book, has_stock in zip(books, in_stock) if has_stock]
