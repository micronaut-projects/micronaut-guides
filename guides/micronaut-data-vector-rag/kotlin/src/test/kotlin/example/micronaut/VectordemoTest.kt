package example.micronaut

import io.micronaut.data.model.vector.search.Score
import io.micronaut.data.model.vector.search.ScoringFunction
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeFalse
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
class VectordemoTest(private val documents: DocumentRepository, private val embeddings: Embeddings) {

    @Test
    fun storesAndFindsNearestVectors() {
        assumeFalse(System.getProperty("org.graalvm.nativeimage.imagecode") != null)
        documents.deleteAll()
        documents.save(document("Micronaut is a JVM framework for fast microservices"))
        documents.save(document("Micronaut Data generates database queries at compile time"))
        documents.save(document("Tomatoes grow well in sunny gardens"))

        val matches = documents.searchTop3ByEmbeddingNear(
            embeddings.embed("JVM framework for microservices"),
            Score(0.7),
            ScoringFunction.COSINE
        ).results()

        assertEquals("Micronaut is a JVM framework for fast microservices", matches.first().entity().content)
        assertTrue(checkNotNull(matches.first().similarity()).value() > .7)
    }

    private fun document(content: String): Document = Document(null, content, embeddings.embed(content))
}
