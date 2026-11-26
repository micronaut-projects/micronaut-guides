from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import Embeddable, GeneratedValue, Id, MappedEntity, Relation
from micronaut.data.annotation.sql import ETaggable, ETagValue, GeneratedETag


# tag::generated-etag[]
@Embeddable
@dataclass
class BookDetails:
    pages: int
    chapters: Annotated[int, ETagValue(exclude=True)]  # <3>


@MappedEntity
@ETaggable  # <1>
@dataclass
class Book:
    title: str
    details: Annotated[BookDetails, Relation(value="EMBEDDED")]
    id: Annotated[int | None, Id, GeneratedValue] = None
    etag: Annotated[str | None, GeneratedETag] = None  # <2>
# end::generated-etag[]
