from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import GeneratedValue, Id, MappedEntity
from micronaut.data.annotation.sql import ETagValue, GeneratedETag


# tag::explicit-etag[]
@MappedEntity
@dataclass
class Article:
    title: Annotated[str, ETagValue]
    notes: str
    id: Annotated[int | None, Id, GeneratedValue, ETagValue] = None
    etag: Annotated[str | None, GeneratedETag(function="SYS_ROW_ETAG")] = None
# end::explicit-etag[]
