from dataclasses import dataclass

from .author import Author


@dataclass
class Book:
    id: str
    name: str
    page_count: int
    author: Author
