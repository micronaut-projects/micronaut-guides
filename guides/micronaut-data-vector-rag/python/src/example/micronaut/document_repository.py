from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.vector import FloatVector
from micronaut.data.model.vector.search import Score, ScoringFunction, SearchResults
from micronaut.data.repository import CrudRepository

from .document import Document


@JdbcRepository(dialect="ORACLE")
class DocumentRepository(CrudRepository[Document, int]):
    def searchTop3ByEmbeddingNear(
        self, vector: FloatVector, maxDistance: Score, scoringFunction: ScoringFunction
    ) -> SearchResults[Document]: ...
