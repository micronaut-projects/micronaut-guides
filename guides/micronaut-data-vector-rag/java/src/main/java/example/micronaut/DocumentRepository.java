package example.micronaut;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.model.vector.FloatVector;
import io.micronaut.data.model.vector.search.Score;
import io.micronaut.data.model.vector.search.ScoringFunction;
import io.micronaut.data.model.vector.search.SearchResults;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.ORACLE)
interface DocumentRepository extends CrudRepository<Document, Long> {

    SearchResults<Document> searchTop3ByEmbeddingNear(
            FloatVector vector,
            Score maxDistance,
            ScoringFunction scoringFunction
    );
}
