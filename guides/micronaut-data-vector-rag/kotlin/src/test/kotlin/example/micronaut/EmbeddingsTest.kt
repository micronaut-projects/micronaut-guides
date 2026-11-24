package example.micronaut

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeFalse
import org.junit.jupiter.api.Test

class EmbeddingsTest {

    @Test
    fun createsMiniLmEmbedding() {
        skipNativeImage()
        assertEquals(Embeddings.DIMENSIONS, Embeddings().embed("Micronaut Data").toFloatArray().size)
    }

    @Test
    fun recognizesSemanticSimilarity() {
        skipNativeImage()
        val embeddings = Embeddings()
        val query = embeddings.embed("lightweight Java toolkit for backend services").toFloatArray()
        val framework = embeddings.embed("Micronaut is a JVM framework for fast microservices").toFloatArray()
        val gardening = embeddings.embed("Tomatoes grow well in sunny gardens").toFloatArray()

        assertTrue(dot(query, framework) > dot(query, gardening))
    }

    private fun dot(left: FloatArray, right: FloatArray): Double =
        left.indices.sumOf { left[it].toDouble() * right[it] }

    private fun skipNativeImage() {
        assumeFalse(System.getProperty("org.graalvm.nativeimage.imagecode") != null)
    }
}
