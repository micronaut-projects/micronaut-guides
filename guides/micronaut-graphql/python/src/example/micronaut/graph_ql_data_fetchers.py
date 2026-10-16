from micronaut.graphql import ResolverInfo
from pyronaut.context import Singleton

from .author import Author
from .book import Book
from .db_repository import DbRepository


@Singleton
class GraphQLDataFetchers:
    def __init__(self, db_repository: DbRepository):  # <1>
        self._db_repository = db_repository

    def book_by_id(  # <2>
        self,
        source: object | None,
        info: ResolverInfo,
        id: str | None = None,  # <3>
    ) -> Book | None:
        return next(  # <4>
            (
                book
                for book in self._db_repository.find_all_books()
                if book.id == id
            ),
            None,
        )

    def author(
        self,
        source: Book,  # <5>
        info: ResolverInfo,
    ) -> Author | None:
        author_book = source.author  # <6>
        return next(  # <7>
            (
                author
                for author in self._db_repository.find_all_authors()
                if author.id == author_book.id
            ),
            None,
        )
