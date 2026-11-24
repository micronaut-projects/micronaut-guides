package example.micronaut;

import io.micronaut.data.model.vector.search.Score;
import io.micronaut.data.model.vector.search.ScoringFunction;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(transactional = false)
class VectordemoTest {

    @Inject
    DocumentRepository documents;

    @Inject
    Embeddings embeddings;

    @Test
    void storesAndFindsNearestVectors() {
        Assumptions.assumeFalse(System.getProperty("org.graalvm.nativeimage.imagecode") != null);
        documents.deleteAll();
        documents.save(document("Micronaut is a JVM framework for fast microservices"));
        documents.save(document("Micronaut Data generates database queries at compile time"));
        documents.save(document("Tomatoes grow well in sunny gardens"));

        var matches = documents.searchTop3ByEmbeddingNear(
                embeddings.embed("JVM framework for microservices"),
                new Score(0.7),
                ScoringFunction.COSINE
        ).results();

        assertEquals(
                "Micronaut is a JVM framework for fast microservices",
                matches.getFirst().entity().content()
        );
        assertTrue(matches.getFirst().similarity().value() > .7);
    }

    private Document document(String content) {
        return new Document(null, content, embeddings.embed(content));
    }
}
