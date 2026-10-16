package example.micronaut

import io.micronaut.data.model.vector.search.Score
import io.micronaut.data.model.vector.search.ScoringFunction
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(transactional = false)
class VectordemoSpec extends Specification {

    @Inject
    DocumentRepository documents

    @Inject
    Embeddings embeddings

    void 'stores and finds nearest vectors'() {
        given:
        documents.deleteAll()
        documents.save(document('Micronaut is a JVM framework for fast microservices'))
        documents.save(document('Micronaut Data generates database queries at compile time'))
        documents.save(document('Tomatoes grow well in sunny gardens'))

        when:
        def matches = documents.searchTop3ByEmbeddingNear(
                embeddings.embed('JVM framework for microservices'),
                new Score(0.7),
                ScoringFunction.COSINE
        ).results()

        then:
        matches.first().entity().content == 'Micronaut is a JVM framework for fast microservices'
        matches.first().similarity().value() > .7
    }

    private Document document(String content) {
        new Document(content: content, embedding: embeddings.embed(content))
    }
}
