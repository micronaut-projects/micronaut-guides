from dataclasses import dataclass
from typing import Annotated

from jakarta.validation import Valid
from jakarta.validation.constraints import NotBlank
from micronaut.data.model.vector import FloatVector
from micronaut.data.model.vector.search import Score, ScoringFunction
from micronaut.http import HttpStatus
from micronaut.http.annotation import Body, Controller, Get, Post, QueryValue, Status
from micronaut.serde.annotation import Serdeable

from .document import Document
from .document_repository import DocumentRepository
from .embeddings import Embeddings


@Serdeable
@dataclass
class DocumentRequest:
    content: Annotated[str, NotBlank]


@Serdeable
@dataclass
class DocumentCreated:
    id: int
    content: str


@Serdeable
@dataclass
class Match:
    id: int
    content: str
    similarity: float


@Controller("/documents")
class DocumentController:
    def __init__(self, documents: DocumentRepository, embeddings: Embeddings):
        self.documents = documents
        self.embeddings = embeddings

    @Post
    @Status(HttpStatus.CREATED)
    def create(self, request: Annotated[DocumentRequest, Body, Valid]) -> DocumentCreated:
        embedding = FloatVector(self.embeddings.embed(request.content))  # <1>
        saved = self.documents.save(Document(content=request.content, embedding=embedding))
        return DocumentCreated(saved.id, saved.content)

    @Get("/search")
    def search(self, q: Annotated[str, QueryValue, NotBlank]) -> list[Match]:
        results = self.documents.searchTop3ByEmbeddingNear(
            FloatVector(self.embeddings.embed(q)),  # <2>
            Score(2.0),
            ScoringFunction.COSINE,  # <3>
        )
        return [
            Match(result.entity().id, result.entity().content, result.similarity().value())
            for result in results
        ]
