from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass(frozen=True)
class BookAnalytics:
    book_isbn: str
    count: int
