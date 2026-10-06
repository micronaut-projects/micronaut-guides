from graphql.schema import DataFetchingEnvironment
from jakarta.inject import Singleton

from .author import Author
from .book import Book
from .db_repository import DbRepository


@Singleton
class GraphQLDataFetchers:
    def __init__(self, db_repository: DbRepository):  # <1>
        self._db_repository = db_repository

    def book_by_id(self, data_fetching_environment: DataFetchingEnvironment) -> Book | None:  # <2>
        book_id = data_fetching_environment.getArgument("id")  # <3>
        return next(  # <4>
            (
                book
                for book in self._db_repository.find_all_books()
                if book.id == book_id
            ),
            None,
        )

    def author(self, data_fetching_environment: DataFetchingEnvironment) -> Author | None:
        book = data_fetching_environment.getSource()  # <5>
        author_book = book.author  # <6>
        return next(  # <7>
            (
                author
                for author in self._db_repository.find_all_authors()
                if author.id == author_book.id
            ),
            None,
        )
