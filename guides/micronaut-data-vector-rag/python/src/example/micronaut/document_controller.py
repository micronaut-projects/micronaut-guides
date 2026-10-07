from dataclasses import dataclass
from typing import Annotated

from jakarta.inject import Inject
from micronaut.data.model.vector.search import Score, ScoringFunction
from micronaut.http import HttpResponse, HttpStatus
from micronaut.http.annotation import Body, Get, Post, QueryValue
from micronaut.http.exceptions import HttpStatusException
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.serde.annotation import Serdeable

from .document import Document
from .document_repository import DocumentRepository
from .embeddings import Embeddings

documents: Annotated[DocumentRepository, Inject]
embeddings: Annotated[Embeddings, Inject]


@dataclass
@Serdeable
class DocumentRequest:
    content: str | None = None


@dataclass
@Serdeable
class DocumentCreated:
    id: int
    content: str


@dataclass
@Serdeable
class Match:
    id: int
    content: str
    similarity: float


@ExecuteOn(TaskExecutors.BLOCKING)
@Post("/documents")
def create(request: Annotated[DocumentRequest, Body]) -> HttpResponse[DocumentCreated]:
    content = required(request.content)  # <1>
    saved = documents.save(Document(None, content, embeddings.embed(content)))
    return HttpResponse.created(DocumentCreated(saved.id, saved.content))


@ExecuteOn(TaskExecutors.BLOCKING)
@Get("/documents/search")
def search(q: Annotated[str, QueryValue]) -> list[Match]:
    results = documents.searchTop3ByEmbeddingNear(
        embeddings.embed(required(q)),  # <2>
        Score(2.0),
        ScoringFunction.COSINE,  # <3>
    ).results()
    return [
        Match(result.entity().id, result.entity().content, result.similarity().value())
        for result in results
    ]


def required(text: str | None) -> str:
    if text is None or not text.strip():
        raise HttpStatusException(HttpStatus.BAD_REQUEST, "text is required")
    return text
