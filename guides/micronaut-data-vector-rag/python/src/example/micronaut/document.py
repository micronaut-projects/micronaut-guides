from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import GeneratedValue, Id, MappedEntity, VectorStorage
from micronaut.data.model.vector import FloatVector

from .embeddings import DIMENSIONS


@dataclass
@MappedEntity("documents")
class Document:
    id: Annotated[int | None, Id, GeneratedValue("IDENTITY")]
    content: str
    embedding: Annotated[FloatVector, VectorStorage(length=DIMENSIONS)]
