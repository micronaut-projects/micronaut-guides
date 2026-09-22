from dataclasses import dataclass

from micronaut.core.annotation import Introspected

from .author import Author


@Introspected
@dataclass
class Book:
    id: str
    name: str
    pageCount: int
    author: Author
