from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import GeneratedValue, Id, MappedEntity, VectorStorage
from micronaut.data.model.vector import FloatVector

from .embeddings import DIMENSIONS


@MappedEntity("documents")
@dataclass
class Document:
    content: str
    embedding: Annotated[FloatVector, VectorStorage(length=DIMENSIONS)]
    id: Annotated[int | None, Id, GeneratedValue("IDENTITY")] = None
